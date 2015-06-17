package map_reduce_job1;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;






public class AvgURLsPerPage extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Path input = new Path(args[0]);
		Path temp1 = new Path("output/temp1");
		Path output = new Path(args[1]);

		Job job1 = Job.getInstance();
		job1.setJobName("URLCount");
		job1.setJarByClass(AvgURLsPerPage.class);
		FileInputFormat.addInputPath(job1, input);
		FileOutputFormat.setOutputPath(job1, temp1);
		job1.setMapperClass(Mapper1.class);
		job1.setReducerClass(Reducer1.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(DoubleWritable.class);
		boolean succ = job1.waitForCompletion(true);
		if (! succ) {
			System.out.println("Job1 failed, exiting");
			return -1;
		}

		Job job2 = Job.getInstance();
		job2.setJobName("AvgUrlPage");
		FileInputFormat.setInputPaths(job2, temp1);
		FileOutputFormat.setOutputPath(job2, output);
		job2.setJarByClass(AvgURLsPerPage.class);
		job2.setMapperClass(Mapper2.class);
		job2.setReducerClass(Reducer2.class);
		//job2.setInputFormatClass(KeyValueTextInputFormat.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(DoubleWritable.class);
		job2.setNumReduceTasks(1);
		succ = job2.waitForCompletion(true);

		if (! succ) {
			System.out.println("Job2 failed, exiting");
			return -1;
		}



		return 0;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Usage: map_reduce_job1/AvgURLsPerPage input/tab1.tsv output/result");
			System.exit(-1);
		}
		int res = ToolRunner.run(new Configuration(), new AvgURLsPerPage(), args);
		System.exit(res);
	}



	public static class Mapper1 extends
	Mapper<LongWritable, Text, Text, DoubleWritable> {

		private static final DoubleWritable one = new DoubleWritable(1);
		private Text word = new Text();

		@Override
		protected void map(LongWritable key, Text value, 
				Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String first_line = "TREC-ID\tSTRINGA\tTAG\n";
			line = line.replaceAll(first_line, "");

			String[] tokenizer = line.split("\t");

			if(tokenizer[2].equals("#URL")){
				word.set(tokenizer[0]);
				context.write(word, one);
			}

		}
	}

	public static class Reducer1 extends
	Reducer<Text, DoubleWritable, Text, DoubleWritable> {


		@Override
		protected void reduce(Text key, Iterable<DoubleWritable> values, 
				Context context) throws IOException, InterruptedException {

			double sum = 0;

			for (DoubleWritable value : values) {
				sum += value.get();
			}

			context.write(key, new DoubleWritable(sum));

		}

	}

	public static class Mapper2 extends
	Mapper<LongWritable, Text, Text, DoubleWritable> {


		private final Text word = new Text("avg");

		@Override
		protected void map(LongWritable key, Text value, 
				Context context) throws IOException, InterruptedException {
			String line = value.toString();

			String[] tokenizer = line.split("\t");
			context.write(word,new DoubleWritable(Double.parseDouble(tokenizer[1])));

		}
	}


	public static class Reducer2 extends
	Reducer<Text, DoubleWritable, Text, DoubleWritable> {


		@Override
		protected void reduce(Text key, Iterable<DoubleWritable> values, 
				Context context) throws IOException, InterruptedException {

			double sum = 0;
			double cont = 0;

			for (DoubleWritable value : values) {
				sum += value.get();
				cont = cont +1;
			}
			System.out.println("---------------- COUNT= " + cont);
			System.out.println("---------------- SUM= " + sum);
			double avg = sum/cont;
			context.write(key, new DoubleWritable(avg));

		}
	}
}
