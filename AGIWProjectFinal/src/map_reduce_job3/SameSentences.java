package map_reduce_job3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class SameSentences extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Path input = new Path(args[0]);
		Path temp1 = new Path("output/temp1");
		Path output = new Path(args[1]);

		Job job1 = Job.getInstance();
		job1.setJobName("SameS");
		job1.setJarByClass(SameSentences.class);
		FileInputFormat.addInputPath(job1, input);
		FileOutputFormat.setOutputPath(job1, output);
		job1.setMapperClass(Mapper1.class);
		job1.setReducerClass(Reducer1.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		boolean succ = job1.waitForCompletion(true);
		if (! succ) {
			System.out.println("Job1 failed, exiting");
			return -1;
		}

		//	Job job2 = Job.getInstance();
		//		job2.setJobName("BestURL");
		//		FileInputFormat.setInputPaths(job2, temp1);
		//		FileOutputFormat.setOutputPath(job2, output);
		//		job2.setJarByClass(SameSentences.class);
		//		job2.setMapperClass(Mapper2.class);
		//		job2.setReducerClass(Reducer2.class);
		//		//job2.setInputFormatClass(KeyValueTextInputFormat.class);
		//		job2.setOutputKeyClass(Text.class);
		//		job2.setOutputValueClass(IntWritable.class);
		//		job2.setNumReduceTasks(1);
		//		succ = job2.waitForCompletion(true);
		//
		//		if (! succ) {
		//			System.out.println("Job2 failed, exiting");
		//			return -1;
		//		}



		return 0;
	}
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Usage: map_reduce_job3/SameSentences input/tab3.tsv output/result");
			System.exit(-1);
		}
		int res = ToolRunner.run(new Configuration(), new SameSentences(), args);
		System.exit(res);
	}



	public static class Mapper1 extends
	Mapper<LongWritable, Text, Text, Text> {

		private static final IntWritable one = new IntWritable(1);
		private Text word = new Text();

		@Override
		protected void map(LongWritable key, Text value, 
				Context context) throws IOException, InterruptedException {
			String line = value.toString();

			String[] tokenizer = line.split("\t");
			String sentence = "";
			for (int i = 1; i < tokenizer.length; i++ ) {
				sentence = sentence.concat(tokenizer[i] + " ");
			}

			word.set(sentence);
			context.write(word, new Text(tokenizer[0]));


		}
	}

	public static class Reducer1 extends
	Reducer<Text, Text, Text, IntWritable> {

		private class Pair {
			public String str;
			public Integer count;

			public Pair(String str, Integer count) {
				this.str = str;
				this.count = count;
			}
		};
		private PriorityQueue<Pair> queue;

		private static final int TOP_K = 30;


		@Override
		protected void reduce(Text key, Iterable<Text> values, 
				Context context) throws IOException, InterruptedException {

			String IDs = "";

			for (Text value : values) {
				IDs = IDs.concat(value.toString() + "\t");
			} 
			String[] arr = IDs.split("\t");
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++){
				if (!idList.contains(arr[i])){
					idList.add(arr[i]);
				}
			}

			//context.write(key, new IntWritable(idList.size()));
			queue.add(new Pair(key.toString(), idList.size()));
			if (queue.size() > TOP_K) {
				queue.remove();
			}

		}


		@Override
		protected void cleanup(
				Reducer<Text, Text, Text, IntWritable>.Context context)
						throws IOException, InterruptedException {
			List<Pair> topKPairs = new ArrayList<Pair>();
			while (! queue.isEmpty()) {
				topKPairs.add(queue.remove());
			}
			for (int i = topKPairs.size() - 1; i >= 0; i--) {
				Pair topKPair = topKPairs.get(i);

				context.write(new Text(topKPair.str), 
						new IntWritable(topKPair.count));
			}
		}


		@Override
		protected void setup(
				Reducer<Text, Text, Text, IntWritable>.Context context)
						throws IOException, InterruptedException {
			queue = new PriorityQueue<Pair>(TOP_K, new Comparator<Pair>() {
				public int compare(Pair p1, Pair p2) {
					return p1.count.compareTo(p2.count);
				}
			});
		}
	}



}

