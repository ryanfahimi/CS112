
/*********************************************************
 * filename: TestTree.java
 *
 * A file that builds a binary tree, fills it with
 * integers read from a file, does some operations
 * on the tree, and prints the results.
 *********************************************************/

import java.io.File;
import java.util.Scanner;

/**
 * class Tree
 *
 * A binary tree data structure that stores TreeNodes.
 * Can:
 * add new TreeNodes to the tree,
 * sum all the values in the tree,
 * find minimum value in the tree,
 * find total number of entries in the tree,
 * find maximum depth of the tree.
 */
class Tree {
	/**
	 * class TreeNode - a private member class of Tree
	 *
	 * A node in class Tree.
	 * Stores a value, and references to zero, one, or two children.
	 */
	private class TreeNode {
		int value;
		TreeNode left;
		TreeNode right;

		TreeNode(int v) {
			value = v;
			left = null;
			right = null;
		}

		int value() {
			return value;
		}
	}

	//////
	////// Class members of Tree
	//////
	TreeNode root;

	//////
	////// Class methods
	//////
	public Tree() {
		root = null;
	}

	/**
	 * Add an int value to the tree
	 * 
	 * @param input the int value to be added to the tree
	 */
	public void add(int input) {
		TreeNode n = new TreeNode(input);

		if (root == null) {
			root = n;
			return;
		}
		add(root, input);
	}

	private void add(TreeNode top, int v) {
		// Put the new node into the tree, in "sorted order".
		// Nodes are added "to the left" of nodes with greater values
		// and "to the right" of nodes with smaller values.
		//
		// 'top' should not be null
		if (v > top.value()) {
			if (top.right == null) {
				top.right = new TreeNode(v);
			} else {
				add(top.right, v);
			}
		} else {
			if (top.left == null) {
				top.left = new TreeNode(v);
			} else {
				add(top.left, v);
			}
		}
	}

	public int sum() {
		return sum(root);
	}

	private static int sum(TreeNode n) {
		if (n == null) {
			return 0;
		}
		return n.value() + sum(n.left) + sum(n.right);
	}

	public int size() {
		return size(root);
	}

	private static int size(TreeNode n) {
		if (n == null) {
			return 0;
		}
		return 1 + size(n.left) + size(n.right);
	}

	public int depth() {
		return depth(root);
	}

	private static int depth(TreeNode n) {
		if (n == null) {
			return 0;
		}
		return 1 + Math.max(depth(n.left), depth(n.right));
	}

	/**
	 * @return minimum value stored in the tree.
	 *
	 *         If the tree is empty, throw an Exception.
	 */
	public int min() throws Exception {
		if (root == null) {
			throw new Exception("TreeMin() called with null arg");
		}
		return min(root);
	}

	private static int min(TreeNode n) {
		if (n.left == null) {
			return n.value();
		}
		return min(n.left);
	}
}

public class TestTree {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("ERROR: need a filename");
			System.exit(0);
		}
		Tree myTree = new Tree();
		// STUDENTS FILL IN CODE TO READ INTS FROM A FILE WHOSE
		// FILENAME IS IN args[0] AND ADD THEM TO THE TREE.
		try (Scanner scanner = new Scanner(new File(args[0]))) {
			while (scanner.hasNextInt()) {
				int value = scanner.nextInt();
				myTree.add(value);
			}
		}

		System.out.println("Sum is " + myTree.sum());
		System.out.println("Min is " + myTree.min());
		System.out.println("Size is " + myTree.size());
		System.out.println("Depth is " + myTree.depth());
	}
}
