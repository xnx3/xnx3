package com.xnx3;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import com.xnx3.file.FileUtil;


public class Test {
	public static void main(String[] args) {
		
		try {
			FileUtil.downFiles("http://res.weiunity.com/template/qiye1/images/icon-sns-zh.gif", "/Users/apple/Desktop/icon-sns-zh.gif");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
