package com.runnningsnail.rxjavademo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongjie created on 2019-11-07.
 */
public class Plan {
	private String time;
	private String content;
	private List<String> actionList = new ArrayList<>();

	public Plan(String time, String content) {
		this.time = time;
		this.content = content;
		for (int i = 0; i < 10; i++) {
			actionList.add(time + content + "_" + i);
		}
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getActionList() {
		return actionList;
	}

	public void setActionList(List<String> actionList) {
		this.actionList = actionList;
	}
}
