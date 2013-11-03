/***************************************************************************************************

Copyright 2013 Shaola Ren
Licensed under the Apache License, Version 2.0;
http://www.apache.org/licenses/LICENSE-2.0

Using Introduction to Algorithm(Third Edition, Thomas H.Cormen, 
Charles E.Leiserson, Ronald L.Rivest, Clifford Stein) as a reference

Organized and coded by Shaola Ren @ November, 2013

Java API: Augmenting Red Black Tree, beside the red black trees properties, 
plus maitain each node's size during insert() and delete(), allow duplicates

Usage:

constructor(1):
public class RBTree<T extends Comparable<T>>
eg: RBTree<T> root = new RBTree<T>()

manipulate class method(2):
public void insert(T key), insert key to a tree, O(lg(n)) theoretically,
						   in practice, may plus O(h) maitain node's size 
						   time, and at most 3 constant time rotated 
						   operations, h is the height of tree
eg: root.insert(key), insert key to the tree rooted from root

public void delete(T key), delete key from a tree if exists,
                           else throw NoSuchElementException
						   O(lg(n)) theoretically,
						   in practice, may plus O(h) maitain node's size 
						   time, and at most 3 constant time rotated 
						   operations, h is the height of tree

eg: root.delete(key), delete key from the tree rooted from root if exists,
	else throw NoSuchElementException

other class methods(17):
public void inorderT(), print out tree's entry through inorder traverse 
                        along with the size of subtree rooted from each
						node and the color of this node, and also this node
						parent with properties color and size.
						avoid to use it when the tree's size is large, it will
						print out lots stuff
						running time O(n)
eg: root.inorderT()

public void check(), check one property of red black tree, if a node is red, 
					 then both its children are black
					 and check each node's size is compatible
eg: root.check()					 

public ArrayList<ArrayList<T>> getAllPathToLeave(), get all paths from root to leaves
								time O(n)
eg: root.getAllPathToLeave()

public ArrayList<ArrayList<T>> getAllPathToLeaveOfNode(T key), get all path from the first
					 node encountered in inorder traverse order has the same value with 
					 key to the leaves rooted from this node
eg: root.getAllPathToLeaveOfNode(key)

public ArrayList<T> getLeaves(): get all leaves of this tree, O(n)
eg: root.getLeaves()

public ArrayList<T> getLeavesOfNode(T key), get all leaves rooted from the first node 
					 encountered in inorder traverse order has the same value with key
eg: root.getLeavesOfNode(key)
					 
public T previous(T key), get the prvious key of the first node 
					 encountered in inorder traverse order has the same value with key, 
					 if this key is not in the tree, throw NoSuchElementException
					 O(lg(n))
eg: root.previous(key)
					 
public boolean hasPrevious(T key), whether the first node encountered in inorder traverse 
					 order has the same value with key has a previous node
					 O(lg(n))
eg: root.hasPrevious(key)
					 
public T next(T key), get the next key of the first node 
					 encountered in inorder traverse order has the same value with key, 
					 if this key is not in the tree, throw NoSuchElementException
					 O(lg(n))
eg: root.next()
					 
public boolean hasNext(T key), whether the first node encountered in inorder traverse 
					 order has the same value with key has a next node
					 O(lg(n))
eg: root.hasNext(key)
					 
public int getBlackHeight(), return the BlackHeight of this tree, O(lg(n))
eg: root.getBlackHeight()

public int getRankOfEntry(T key), return the rank the first node encountered in inorder traverse 
					 order has the same value with key, if this key is not in the tree, 
					 throw NoSuchElementException
					 O(lg(n))
eg: root.getRankOfEntry(key)
					 
public T getKthEntry(int k), return the kth smallest value, O(lg(n))
eg: root.getKthEntry(k)

public int size(), return the size of this tree, O(1)
eg: root.size()

public boolean contains(T key), whether the tree contains this key, contains true, else false
								O(lg(n))
eg: root.contains(key)
								
public T min(), return minimum value of this tree in natural order, O(lg(n))
eg: root.min()

public T max(), return maximum value of this tree in natural order, O(lg(n))
eg: root.max()

***************************************************************************************************/


import java.util.*;
import java.util.NoSuchElementException;

public class RBTree<T extends Comparable<T>>{
	private static final boolean RED   = true;
    private static final boolean BLACK = false;
	
	private Node<T> root; //root of RBTree
	
	private class Node<T>{
		private T val;
		private Node<T> left, right, parent;
		private boolean color;
		private boolean visited;
		private int N; //number of nodes rooted from this node
		public Node(T v){
			val = v;
		}
		public Node(T v, boolean c, int n){
			val = v;
			color = c;
			N = n;
		}
		
		public Node<T> copyNode(){
			Node<T> copy = new Node<T>(this.val);
			copy.left = this.left;
			copy.right = this.right;
			copy.parent = this.parent;
			copy.color = this.color;
			copy.N = this.N;
			copy.visited = this.visited;
			return copy;
		}
	}
	
	// insert node to red black tree
	
	public void insert(T key){
		root = insert(root, key);
	}
	
	private Node<T> insert(Node<T> x, T key){
		Node<T> z = new Node<T>(key, RED, 0);
		Node<T> y = null;
		while(x!=null){
			y = x;
			if(z.val.compareTo(x.val)<0) x = x.left;
			else x = x.right;
		}
		z.parent = y;
		// fix N, use a probe pointer reference to z;
		Node<T> probe = z;
		while(probe!=null){
			probe.N = probe.N + 1;
			probe = probe.parent;
		}
		
		if(y==null) root = z;
		else if(z.val.compareTo(y.val)<0) y.left = z;
		else y.right = z;
		insertFixup(z);
		root.color = BLACK;
		return root;
	}
	
	private void insertFixup(Node<T> z){
		if(z.parent!=null){
			while(z.parent!=null && z.parent.color){
				if(z.parent.parent!=null && z.parent==z.parent.parent.left){
					Node<T> y = z.parent.parent.right;
					if(y!=null && y.color){
						z.parent.color = BLACK;
						y.color = BLACK;
						z.parent.parent.color = RED;
						z = z.parent.parent;
					}
					else{
						if(z==z.parent.right){
						z = z.parent;
						leftRotate(z);
						}
						//if(z.parent!=null) z.parent.color = BLACK;
						if(z.parent!=null && z.parent.parent!=null){
							z.parent.color = BLACK;
							z.parent.parent.color = RED;
							rightRotate(z.parent.parent);
						}
					}
				}
				else if(z.parent.parent!=null && z.parent==z.parent.parent.right){
					Node<T> y = z.parent.parent.left;
					if(y!=null && y.color){
						z.parent.color = BLACK;
						y.color = BLACK;
						z.parent.parent.color = RED;
						z = z.parent.parent;
					}
					else{
						if(z==z.parent.left){
							z = z.parent;
							rightRotate(z);
						}
						if(z.parent!=null && z.parent.parent!=null){
							z.parent.color = BLACK;
							z.parent.parent.color = RED;
							leftRotate(z.parent.parent);
						}
					}
				}
			}
		}
	}
	
	// delete red black tree's node
	
	public void delete(T key){
		root = deletePrivate(key);
	}
	
	private Node<T> deletePrivate(T key){
		Node<T> z = search(root, key);
		if(z==null) throw new NoSuchElementException("not exist");
		Node<T> y = z;
		Node<T> x = null;
		boolean yOriginalC = y.color;
		if(z.left==null){
			x = z.right;
			if(x==null){
				x = z.copyNode();
				x.left = null;
				x.right = null;
				x.N = 0;
				x.color = BLACK;
				x.visited = false;
				// rebuild parent child relation
				z.right = x;
				transplant(z, x);
			}
			else transplant(z, z.right);
		}
		else if(z.right==null){
			x = z.left;
			if(x==null){
				x = z.copyNode();
				x.left = null;
				x.right = null;
				x.N = 0;
				x.color = BLACK;
				x.visited = false;
				// rebuild parent child relation
				z.left = x;
				transplant(z, x);
			}
			else transplant(z, z.left);
		}
		else{
			y = min(z.right);
			yOriginalC = y.color;
			x = y.right;
			if(x==null){
				x = y.copyNode();
				x.left = null;
				x.right = null;
				x.N = 0;
				x.color = BLACK;
				x.visited = false;
				// rebuild parent child relation
				y.right = x;
			}
			
			if(y.parent==z){
				x.parent = y;
			}
			else{
				if(y.right==null){ transplant(y, x);}
				else{ transplant(y, y.right);}
				y.right = z.right;
				y.right.parent = y;
			}
			transplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
			z = null;
			// fix N
			while(y!=null){
				if(y.left!=null && y.right!=null){
					y.N = y.left.N + y.right.N + 1;
				}
				else if(y.left!=null) y.N = y.left.N + 1;
				else if(y.right!=null) y.N = y.right.N + 1;
				else y.N = 1;
				y = y.parent;
			}
		}
		if(!yOriginalC) deleteFixup(x);
		if(x.N==0){
			if(x.parent==null) root = null;
			else if(x == x.parent.left)
				x.parent.left = null;
			else x.parent.right = null;
		}
		return root;
	}
	
	private void deleteFixup(Node<T> x){
		while(x!=root && x!=null && !x.color){
			if(x.parent!=null && x==x.parent.left){
				Node<T> w = x.parent.right;
				if(w!=null && w.color){
					w.color = BLACK;
					x.parent.color = RED;
					leftRotate(x.parent);
					w = x.parent.right;
				}
				if(w!=null && w.left!=null && !w.left.color && w.right!=null && !w.right.color){
					w.color = RED;
					x = x.parent;
				}
				else{
					if(w!=null && w.right!=null && !w.right.color){
						if(w.left!=null){
							w.left.color = BLACK;
							w.color = RED;
							rightRotate(w);
							w = x.parent.right;
						}
					}
					if(w!=null){
						w.color = x.parent.color;
						x.parent.color = BLACK;
						if(w.right!=null) w.right.color = BLACK;
						leftRotate(x.parent);
					}
					x = root;
				}
			}
			else if(x.parent!=null && x==x.parent.right){
				Node<T> w = x.parent.left;
				if(w!=null && w.color){
					w.color = BLACK;
					x.parent.color = RED;
					rightRotate(x.parent);
					w = x.parent.left;
				}
				if(w!=null && w.right!=null && !w.right.color && w.left!=null && !w.left.color){
					w.color = RED;
					x = x.parent;
				}
				else{
					if(w!=null && w.left!=null && !w.left.color){
						if(w.right!=null){
							w.right.color = BLACK;
							w.color = RED;
							leftRotate(w);
							w = x.parent.left;
						}
					}
					if(w!=null){
						w.color = x.parent.color;
						x.parent.color = BLACK;
						if(w.left!=null) w.left.color = BLACK;
						rightRotate(x.parent);
					}
					x = root;
				}
			}
		}
		if(x!=null) x.color = BLACK;
	}
	
	private Node<T> search(Node<T> x, T key){
		if(x==null) return null;
		if(x.val.compareTo(key)<0) return search(x.right, key);
		else if(x.val.compareTo(key)>0) return search(x.left, key);
		else return x;
	}
	
	// leftRotate, rightRotate, transplant
	
	private void leftRotate(Node<T> x){
		// fix N to the final state first
		Node<T> y = x.right;
		int backupxN = x.N;
		if(y.left!=null) x.N = x.N - y.N + y.left.N;
		else x.N = x.N -y.N;
		y.N = backupxN;
		
		x.right = y.left;
		if(y.left!=null) y.left.parent = x;
		y.parent = x.parent;
		if(x.parent==null) root = y;
		else if(x==x.parent.left) x.parent.left = y;
		else x.parent.right = y;
		y.left = x;
		x.parent = y;
	}
	
	private void rightRotate(Node<T> y){
		// fix N to the final state first
		Node<T> x = y.left;
		int backupyN = y.N;
		if(x.right!=null) y.N = y.N - x.N + x.right.N;
		else y.N = y.N - x.N;
		x.N = backupyN;
		
		y.left = x.right;
		if(x.right!=null) x.right.parent = y;
		x.parent = y.parent;
		if(y.parent==null) root = x;
		else if(y==y.parent.left) y.parent.left = x;
		else y.parent.right = x;
		x.right = y;
		y.parent = x;
	}
	
	private void transplant(Node<T> u, Node<T> v){
		if(u.parent==null) root = v;
		else if(u==u.parent.left) u.parent.left = v;
		else u.parent.right = v;
		v.parent = u.parent;
		
		// fix N
		Node<T> probe = v.parent;
		while(probe!=null){
			probe.N = probe.N - 1;
			probe = probe.parent;
		}
	}
	
	// traverse tree to verify by eye
	
	public void inorderT(){
		inorderT(root);
	}
	
	private void inorderT(Node<T> root){
		if(root==null) return;
		inorderT(root.left);
		System.out.println(root.val + " size: " + root.N + " color: " + root.color);
		if(root.parent!=null) System.out.println(root.val + " parent: " + root.parent.val + " color: " + root.parent.color);
		inorderT(root.right);
	}
	
	// verify Red Black Tree
	
	public void check(){
		if(root==null) return;
		System.out.println("root.val: " + root.val + " root.N: " + root.N);
		System.out.println("RB tree's black height: " + getBlackHeight(root));
		if(check(root)) System.out.println("true RB tree");
		else System.out.println("false RB tree");
	}
	
	private boolean check(Node<T> root){
		if(root==null) return true;
		if(root.left!=null && root.right!=null){
			if(root.N!=root.left.N+root.right.N+1) return false;
			if(root.color &&(root.left.color || root.right.color)) return false;
			return check(root.left) && check(root.right);
		}
		else if(root.left!=null){
			if(root.N!=root.left.N+1) return false;
			if(root.color && root.left.color) return false;
			return check(root.left);
		}
		else if(root.right!=null){
			if(root.N!=root.right.N+1) return false;
			if(root.color && root.right.color) return false;
			return check(root.right);
		}
		else{
			if(root.N!=1) return false;
		}
		return true;
	}
	// isBalanced serial needs to check??????????? && delete function 
	public boolean isBalanced(){
		return isBalancedTree(root);
	}
	
	private boolean isBalancedTree(Node<T> x){
		if(x==null) return true;
		Node<T> y = min(x);
		while(y!=null){
			if(!isBalanced(y)) return false;			
			y = next(y);
		}
		return true;
	}
	
	private boolean isBalanced(Node<T> x){
		if(x==null) return true;
		ArrayList<Node<T>> leaves = getNodeLeaves(x);
		if(leaves.isEmpty()) return true;
		int[] heights = new int[leaves.size()];
		int index = 0;
		for(Node<T> node : leaves){
			heights[index++] = getBlackHeightFromLeaveToNode(node, x);
		}
		for(int i=1; i<index; i++){
			if(heights[i]!=heights[0]){
				//System.out.println(heights[0] + "    " + heights[i]);
				return false;
			}
		}
		return true;
	}
	
	private int getBlackHeightFromLeaveToNode(Node<T> leave, Node<T> node){
		int height = 0;
		while(leave!=node){
			if(!leave.color) height++;
			leave = leave.parent;
		}
		if(!node.color) height++;
		return height;
	}
	
	private int getBlackHeight(Node<T> r){
		if(r==null) return 0;
		int height = 0;
		while(r!=null){
			if(!r.color) height++;
			if(r.left!=null) r = r.left;
			else if(r.right!=null) r = r.right;
			else r = null;
		}
		return height;
	}
	
	// this one is the same as the above one
	private int getBHeight(Node root){
        if(root==null) return 0;
		int height = 0;
		if(!root.color) height = Math.max(getBHeight(root.left), getBHeight(root.right)) + 1;
		else height = Math.max(getBHeight(root.left), getBHeight(root.right));
        return height;
    }
	
	// additional utility functions
	
	public ArrayList<ArrayList<T>> getAllPathToLeave(){
		return getAllPathToLeaveOfNode(root);
	}
	
	public ArrayList<ArrayList<T>> getAllPathToLeaveOfNode(T key){
		Node<T> x = search(root, key);
		return getAllPathToLeaveOfNode(x);
	}
	
	private ArrayList<ArrayList<T>> getAllPathToLeaveOfNode(Node<T> x){
		ArrayList<T> sublist = new ArrayList<T>();
		return getAllPathToLeaveOfNode(x, sublist);
	}
	
	private ArrayList<ArrayList<T>> getAllPathToLeaveOfNode(Node<T> x, ArrayList<T> sublist){
		ArrayList<ArrayList<T>> list = new ArrayList<ArrayList<T>>();
		if(x==null) return list;
		if(x.left==null && x.right==null){
			sublist.add(x.val);
			ArrayList<T> tmp = new ArrayList<T>(sublist);
			sublist.remove(sublist.size()-1);
			list.add(tmp);
			return list;
		}
		sublist.add(x.val);
		list.addAll(getAllPathToLeaveOfNode(x.left, sublist));
		list.addAll(getAllPathToLeaveOfNode(x.right, sublist));
		sublist.remove(sublist.size()-1);
		return list;
	}
	
	public ArrayList<T> getLeaves(){
		return getLeavesOfNode(root);
	}
	
	public ArrayList<T> getLeavesOfNode(T key){
		Node<T> x = search(root, key);
		return getLeavesOfNode(x);
	}
	
	private ArrayList<T> getLeavesOfNode(Node<T> x){
		ArrayList<T> list = new ArrayList<T>();
		list = getLeaves(x);
		return list;
	}
	
	private ArrayList<T> getLeaves(Node<T> x){
		ArrayList<T> list = new ArrayList<T>();
		if(x==null) return list;
		if(x.left==null && x.right==null) list.add(x.val);
		list.addAll(getLeaves(x.left));
		list.addAll(getLeaves(x.right));
		return list;
	}
	
	private ArrayList<Node<T>> getNodeLeaves(Node<T> x){
		ArrayList<Node<T>> list = new ArrayList<Node<T>>();
		if(x==null) return list;
		if(x.left==null && x.right==null) list.add(x);
		list.addAll(getNodeLeaves(x.left));
		list.addAll(getNodeLeaves(x.right));
		return list;
	}
	
	public T previous(T key){
		Node<T> x = search(root, key);
		Node<T> y = previous(x);
		if(y==null) throw new NoSuchElementException("this entry has no predecessor");
		return y.val;
	}
	
	public boolean hasPrevious(T key){
		Node<T> x = search(root, key);
		Node<T> y = previous(x);
		if(y==null) return false;
		return true;
	}
	
	private Node<T> previous(Node<T> x){
		if(x==null) throw new NoSuchElementException("the entry is not in this tree");
		if(x.left!=null) return max(x.left);
		Node<T> y = x.parent;
		while(y!=null && x==y.left){
			x = y;
			y = y.parent;
		}
		return y;
	}
	
	public T next(T key){
		Node<T> x = search(root, key);
		Node<T> y = next(x);
		if(y==null) throw new NoSuchElementException("this entry has no successor");
		return y.val;
	}
	
	public boolean hasNext(T key){
		Node<T> x = search(root, key);
		Node<T> y = next(x);
		if(y==null) return false;
		return true;
	}
	
	private Node<T> next(Node<T> x){
		if(x==null) throw new NoSuchElementException("the entry is not in this tree");
		if(x.right!=null) return min(x.right);
		Node<T> y = x.parent;
		while(y!=null && x==y.right){
			x = y;
			y = y.parent;
		}
		return y;
	}
	
	public int getBlackHeight(){
		return getBlackHeight(root);
	}
	
	public int getRankOfEntry(T key){
		Node<T> x = search(root, key);
		return getRankOfEntry(root, x);
	}
	
	// red black tree rooted from node
	private int getRankOfEntry(Node<T> node, Node<T> x){
		if(x==null) throw new NoSuchElementException("the entry is not in this tree");
		int tmp = 0;
		if(x.left!=null) tmp = x.left.N + 1;
		else tmp = 1;
		Node<T> y = x;
		while(y!=node){
			if(y==y.parent.right){
				if(y.parent.left!=null) tmp = tmp + y.parent.left.N + 1;
				else tmp = tmp + 1;
			}
			y = y.parent;
		}
		return tmp;
	}
	
	public T getKthEntry(int k){
		Node<T> node = getKthEntry(root, k);
		return node.val;
	}
	
	private Node<T> getKthEntry(Node<T> x, int k){
		if(k==0 || x==null || k>x.N) throw new NoSuchElementException("k exists size of tree");
		int tmp = 0;
		if(x.left!=null) tmp = x.left.N + 1;
		else tmp = 1;
		if(tmp==k) return x;
		else if(tmp>k) return getKthEntry(x.left, k);
		else return getKthEntry(x.right, k-tmp);
	}
	
	public int size(){
		return root.N;
	}
	
	public boolean contains(T key){
		Node<T> node = search(root, key);
		if(node==null) return false;
		return true;
	}
	
	public T min(){
		Node<T> x = min(root);
		if(x==null) throw new NoSuchElementException("not exist");
		return x.val;
	}
	
	private Node<T> min(Node<T> x){
		while(x!=null && x.left!=null) x = x.left;
		return x;
	}
	
	public T max(){
		Node<T> x = max(root);
		if(x==null) throw new NoSuchElementException("not exist");
		return x.val;
	}
	
	private Node<T> max(Node<T> x){
		while(x!=null && x.right!=null) x = x.right;
		return x;
	}
	
	public static void main(String[] args){
		///********************************************************
		RBTree<Integer> root = new RBTree<Integer>();
		Random rnd = new Random();
		
		int num = 200000;
		int[] A = new int[num];
		for(int i=0; i<num; i++){
			A[i] = rnd.nextInt(10000);
			//System.out.println("A: " + A[i]);
		}
		int deletenum = 2000;
		int[] B = new int[deletenum];
		
		System.out.println("building RB Tree:");
		long starttime = System.currentTimeMillis();
		for(int i=0; i<num; i++){
			root.insert(A[i]);
		}
		long finishtime = System.currentTimeMillis();
		long elapsetime = finishtime - starttime;
		System.out.println();
		System.out.println("elapsed time: " + elapsetime + "ms");
		//root.inorderT();
		root.check();
		
		for(int i=0; i<deletenum; i++){
			int index = rnd.nextInt(num);
			while(A[index]==-1) index = rnd.nextInt(num);
			B[i] = A[index];
			A[index] = -1;
			//System.out.println("B: " + B[i]);
		}
		
		System.out.println("deleting nodes in RB Tree:");
		starttime = System.currentTimeMillis();
		for(int i=0; i<deletenum; i++){
			root.delete(B[i]);
		}
		finishtime = System.currentTimeMillis();
		elapsetime = finishtime - starttime;
		System.out.println();
		System.out.println("elapsed time: " + elapsetime + "ms");
		//root.inorderT();
		root.check();
		
		//**************************************************************/
		
		/*RBTree<Character> root = new RBTree<Character>();
		char[] set = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
			'o','p','q','r','s','t','u','v','w','x','y','z'};
		for(int i=0; i<26; i++) root.insert(set[i]);
		for(int i=0; i<7; i++) root.delete(set[4*i]);
		root.inorderT();
		root.check();*/
	}
}