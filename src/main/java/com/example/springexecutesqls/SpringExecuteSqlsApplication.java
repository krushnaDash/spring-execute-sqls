package com.example.springexecutesqls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.opencsv.CSVWriter;

@SpringBootApplication
public class SpringExecuteSqlsApplication implements CommandLineRunner {
	
	@Value("${input_dir}")
	String sqlPath;
	
	@Autowired
	JdbcTemplate template;
	
	Logger log=LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(SpringExecuteSqlsApplication.class, args);
	}
	
	public Map<String,String> executeSqlFromPath(String path) throws IOException{
		Map<String,String> results= new HashMap<>();
		File[] sqlFiles=  new File(path).listFiles(f-> f.getName().endsWith(".sql") );
		for(File f: sqlFiles) {
			log.info("Running SQL from file {}",f.getName());
			String sql=FileUtils.readFileToString(f,StandardCharsets.UTF_8);
			// remove the semicolon from end of the string
			sql=sql.trim().endsWith(";") ?sql.trim().substring(0, sql.length()-1):sql;
			long timeInMs=executeSqlAndGetTime(sql);
			results.put(f.getName(), timeInMs !=-1 ?String.valueOf(timeInMs/1000):"Could not Execute SQL");
		}
		return results;
	}
	
	public long executeSqlAndGetTime(String sql) {
		try {
			long starttime = System.currentTimeMillis();
			template.queryForList(sql);
			return System.currentTimeMillis() - starttime;
		} catch (Exception e) {
			log.error("Exception on SQL execution",e);
			return -1;
		}
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		Map<String,String> results=executeSqlFromPath(sqlPath);
		CSVWriter csvWriter= new CSVWriter(new FileWriter("report.csv"));
		csvWriter.writeNext(new String[] {"File Name", "Time taken in Seconds"});
		for(String key:results.keySet()) {
			csvWriter.writeNext(new String[] {key, results.get(key)});
		}
		csvWriter.close();
	}
	
	
	
	
	
	

}
