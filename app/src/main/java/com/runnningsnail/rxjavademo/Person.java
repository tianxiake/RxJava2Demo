package com.runnningsnail.rxjavademo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongjie created on 2019-11-07.
 */
public class Person {

	private String name;
	private List<Plan> planList = new ArrayList<>();

	public Person(String name) {
		this.name = name;
		for (int i = 0; i < 1; i++) {
			planList.add(new Plan(name, name));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Plan> getPlanList() {
		return planList;
	}

	public void setPlanList(List<Plan> planList) {
		this.planList = planList;
	}

}
