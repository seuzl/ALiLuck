package com.taobao.lottery.biz.chain.verity;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.impl.ChainBase;

import java.util.List;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class ScanCodeChain extends ChainBase {

	//验证链
	private List<Command> commands;

	public ScanCodeChain() {
		super();
	}

	public void init() {
		for (Command command : commands) {
			addCommand(command);
		}
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
}
