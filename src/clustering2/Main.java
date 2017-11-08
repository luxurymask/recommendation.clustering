package clustering2;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
	public static void bfs(TreeNode root){
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while(!queue.isEmpty()){
			TreeNode current = queue.poll();
			System.out.println(current.getContent() + " | " +  current.getRow() + " | " +  current.getColumn());
			if(current.hasChild()){
				for(TreeNode child : current.getChildrenList()){
					queue.offer(child);
				}
			}
		}
	}
	
	public static void main(String[] args){
		String graphMLPath = "/Users/liuxl/Desktop/毕业设计-not published/basic data/graphml/task2/2-wuyunfei.xml";
		File graphMLFile = new File(graphMLPath);
		Tree tree = new Tree(graphMLFile);
		TreeNode root = tree.root;
		bfs(root);
	}
}
