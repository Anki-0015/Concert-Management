package Project1;
import java.util.*;

// Node class representing each element in the Binary Search Tree (BST)
class UserNode {
    User user;
    UserNode left, right;

    // Constructor to initialize a node with a user
    public UserNode(User user) {
        this.user = user;
        left = right = null;
    }
}

// Binary Search Tree class to manage User objects
class UserBST {
    private UserNode root;

    // Public method to insert a user into the BST
    public void insert(User user) {
        root = insertRec(root, user);
    }

    // Helper method to recursively insert a user based on userId
    private UserNode insertRec(UserNode root, User user) {
        if (root == null) return new UserNode(user);

        if (user.userId < root.user.userId)
            root.left = insertRec(root.left, user);
        else if (user.userId > root.user.userId)
            root.right = insertRec(root.right, user);
        return root;
    }

    // Public method to search for a user by userId
    public User search(int userId) {
        return searchRec(root, userId);
    }

    // Helper method to recursively search for a user in the BST
    private User searchRec(UserNode root, int userId) {
        if (root == null) return null;
        if (userId == root.user.userId) return root.user;

        return (userId < root.user.userId)
                ? searchRec(root.left, userId)
                : searchRec(root.right, userId);
    }

    // Public method to display all users in sorted order (inorder traversal)
    public void inorderDisplay() {
        inorderRec(root);
    }

    // Helper method for inorder traversal: left-root-right
    private void inorderRec(UserNode root) {
        if (root != null) {
            inorderRec(root.left);
            root.user.displayInfo();
            inorderRec(root.right);
        }
    }

    // ✅ Remove a user by ID
    public void remove(int userId) {
        root = removeRec(root, userId);
    }

    // Public method to remove a user by userId
    private UserNode removeRec(UserNode root, int userId) {
        if (root == null) return null;

        if (userId < root.user.userId) {
            root.left = removeRec(root.left, userId);
        } else if (userId > root.user.userId) {
            root.right = removeRec(root.right, userId);
        } else {

            if (root.left == null && root.right == null) {
                return null;
            }

            if (root.left == null) return root.right;
            if (root.right == null) return root.left;


            UserNode successor = getMin(root.right);
            root.user = successor.user;
            root.right = removeRec(root.right, successor.user.userId);
        }
        return root;
    }

    // Find node with minimum value in a subtree
    private UserNode getMin(UserNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }
}

// Abstract User class
abstract class User {
    protected int userId;
    protected String name;
    protected String email;

    // Constructor to initialize common user fields
    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    // Abstract method to display user information — must be implemented by subclasses
    public abstract void displayInfo();
}