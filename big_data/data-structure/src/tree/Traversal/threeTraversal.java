package tree.Traversal;


import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
class TreeNode{
    TreeNode left;
    TreeNode right;
    int val;
}
public class threeTraversal {

    public List<Integer> preOrderTravel(TreeNode root){
        List<Integer> res = new ArrayList<Integer>();
        Deque<TreeNode> stack = new LinkedList<>();

        while(root!=null||!stack.isEmpty()){
            while(root!=null){
                res.add(root.val);
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            root = root.right;
        }

        return res;
    }

    public List<Integer> inOrderTravel(TreeNode root){
        List<Integer> res = new ArrayList<Integer>();
        Deque<TreeNode> stack = new LinkedList<>();
        while(!stack.isEmpty()||root!=null){
            while(root!=null){
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }

        return res;
    }

    public List<Integer> postOrderTravel(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode pre = null;
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.peek();
            if (root.right == null || root.right == pre) {//防止二次遍历右子树
                res.add(root.val);
                pre = root;//每次都记录上一个节点是谁
                stack.pop();
            } else {
                stack.push(root.right);
                root = root.right;
            }
        }

        return res;
    }

    public List<Integer> postOrderTravel2(TreeNode root) {//前序遍历的逆序（前提是修改前序遍历中的遍历顺序）
        LinkedList<Integer> res = new LinkedList<>();
        Deque<TreeNode> stack = new LinkedList<>();

        while(root!=null||!stack.isEmpty()){
            while(root!=null){
                res.addFirst(root.val);
                stack.push(root);
                root = root.right;
            }
            root = stack.pop();
            root = root.left;
        }

        return res;
    }


}


