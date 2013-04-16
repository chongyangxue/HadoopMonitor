package com.hadoopmonitor;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hadoopmonitor.Printer;

public class Starter {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");;
		Printer printer = (Printer) applicationContext.getBean(Printer.class);
		//printer.printHadoopStatus();
		printer.printRetiredFiles();
	}
}