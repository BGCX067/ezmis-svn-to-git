package com.jteap.wfengine.workflow.actionHandler;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.def.Node.NodeType;
import org.jbpm.graph.exe.ExecutionContext;

/**
 *
 *初始化节点事件
 *node - leave
 */
public class LeaveNodeEventHandler implements ActionHandler {

	private static final long serialVersionUID = 5593827574610795242L;


	public void execute(ExecutionContext context) throws Exception {
		
		
//		String tokenId = context.getToken().getId()+"";
		String pid = context.getToken().getProcessInstance().getId()+"";
		Node toNode = context.getTransition().getTo();
		if(toNode.getNodeType() == NodeType.Task){
			setPreNodeAttribute(toNode,context,pid);
		}else if(toNode.getNodeType().equals(NodeType.Fork)){
			List<Node> destNodes = findForkNodes(toNode);
			for (Node node : destNodes) {
				setPreNodeAttribute(node,context,pid);
			}
		}else if(toNode.getNodeType().equals(NodeType.Join)){
			toNode = toNode.getLeavingTransitions().get(0).getTo();
			setPreNodeAttribute(toNode,context,pid);
		}
	}

	/**
	 * 设置下一步环节相关变量
	 * @param toNode
	 * @param context
	 * @param pid
	 */
	private void setPreNodeAttribute(Node toNode, ExecutionContext context,String pid) {
		String preNodeName = context.getEventSource().getName();
		String preActor = context.getContextInstance().getTransientVariable("actor")+"";
		context.getContextInstance().setVariable(toNode.getId()+"_"+pid+"_preNodeName",preNodeName);
		context.getContextInstance().setVariable(toNode.getId()+"_"+pid+"_preActor", preActor);
	}

	/**
	 * 查询所有参与会签的任务
	 * @param toNode
	 * @return
	 */
	private List<Node> findForkNodes(Node toNode) {
		List<Node> nodes = new ArrayList<Node>();
		List<Transition> trans = toNode.getLeavingTransitionsList();
		for (Transition tran : trans) {
			Node node = tran.getTo();
			if(node.getNodeType().equals(NodeType.Task)){
				nodes.add(node);
			}
		}
		return nodes;
	}
	

}
