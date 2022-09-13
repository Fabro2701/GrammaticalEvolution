package model.module.operator;

import java.util.Properties;

import model.individual.Population;

public class PrintOperator extends Operator{
	String msg;
	public PrintOperator(String s) {
		super(null,null);
		this.msg=s;
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}

}
