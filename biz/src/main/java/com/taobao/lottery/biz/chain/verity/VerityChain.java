package com.taobao.lottery.biz.chain.verity;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.impl.ChainBase;

import java.util.List;

/**
 * Created by qingmian.mw on 2016/8/9.
 */
public class VerityChain extends ChainBase{

	//验证链
	private List<Command> commands;

	public VerityChain() {
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
