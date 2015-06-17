package map_reduce_job4;

import java.io.IOException;

import map_reduce_job1.AvgURLsPerPage;
import map_reduce_job1.AvgURLsPerPage.Mapper1;
import map_reduce_job1.AvgURLsPerPage.Mapper2;
import map_reduce_job1.AvgURLsPerPage.Reducer1;
import map_reduce_job1.AvgURLsPerPage.Reducer2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TagOccurrence extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Path input = new Path(args[0]);
		Path temp1 = new Path("output/temp1");
		Path output = new Path(args[1]);

		Job job1 = Job.getInstance();
		job1.setJobName("TagPercentage");
		job1.setJarByClass(TagOccurrence.class);
		FileInputFormat.addInputPath(job1, input);
		FileOutputFormat.setOutputPath(job1, output);
		job1.setMapperClass(Mapper1.class);
		job1.setCombinerClass(LongSumReducer.class);

		job1.setReducerClass(Reducer1.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(LongWritable.class);
		boolean succ = job1.waitForCompletion(true);
		if (! succ) {
			System.out.println("Job1 failed, exiting");
			return -1;
		}

		//		Job job2 = Job.getInstance();
		//		job2.setJobName("AvgUrlPage");
		//		FileInputFormat.setInputPaths(job2, temp1);
		//		FileOutputFormat.setOutputPath(job2, output);
		//		job2.setJarByClass(AvgURLsPerPage.class);
		//		job2.setMapperClass(Mapper2.class);
		//		job2.setReducerClass(Reducer2.class);
		//		//job2.setInputFormatClass(KeyValueTextInputFormat.class);
		//		job2.setOutputKeyClass(Text.class);
		//		job2.setOutputValueClass(LongWritable.class);
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
			System.out.println("Usage: map_reduce_job4/TagPercentage input/tab1.tsv output/result");
			System.exit(-1);
		}
		int res = ToolRunner.run(new Configuration(), new TagOccurrence(), args);
		System.exit(res);
	}



	public static class Mapper1 extends
	Mapper<LongWritable, Text, Text, LongWritable> {

		private static final LongWritable one = new LongWritable(1);
		private Text word = new Text();
		private static final Text TOTAL_KEY = new Text("TOTAL_KEY");
		@Override
		protected void map(LongWritable key, Text value, 
				Context context) throws IOException, InterruptedException {
			String line = value.toString();

			String[] tokenizer = line.split("\t");

			word.set(tokenizer[2]);
			context.write(word, one);
			context.write(TOTAL_KEY, one);



		}
	}

	public static class Reducer1 extends
	Reducer<Text, LongWritable, Text, LongWritable> {


		@Override
		protected void reduce(Text key, Iterable<LongWritable> values, 
				Context context) throws IOException, InterruptedException {

			long sum = 0;
			for (LongWritable value : values) {
				sum += value.get();
			}

			context.write(key, new LongWritable(sum));

		}
	}
}