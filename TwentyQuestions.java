import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TwentyQuestions {
    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private JButton yesButton;
    private JButton noButton;
    private JButton resetButton;
    private Node current;
    private static final String QUESTION_FILE = "tree.ser"; // file to save tree

    // Node class that nodes in the binary tree
    private static class Node implements Serializable {
        String data;
        Node yes;
        Node no;

        Node(String data) {
            this.data = data;
            this.yes = null;
            this.no = null;
        }
    }

    // constructor for the GUI
    public TwentyQuestions() {
        // Initialize GUI components
        frame = new JFrame("20 Questions Game");
        panel = new JPanel(new BorderLayout());
        questionLabel = new JLabel();
        yesButton = new JButton("Yes");
        noButton = new JButton("No");
        resetButton = new JButton("Reset");

        // panel layout set up
        panel.add(questionLabel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.add(resetButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // adding panel to frame
        frame.add(panel);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // loading the tree from file
        current = loadTree();
        if (current == null) {
            current = constructTree();
        }
        setQuestion(current);

        // action listeners for buttons
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (current != null && current.yes != null) {
                    current = current.yes;
                    setQuestion(current);
                } else {
                    displayResult(true);
                }
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (current != null && current.no != null) {
                    current = current.no;
                    setQuestion(current);
                } else {
                    displayResult(false);
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = constructTree();
                setQuestion(current);
            }
        });
    }

    // setting the question displayed in the GUI
    private void setQuestion(Node node) {
        if (node != null) {
            questionLabel.setText(node.data);
        }
    }

    // handling the user response and updating the tree
    private void displayResult(boolean guessedCorrectly) {
        if (guessedCorrectly) {
            JOptionPane.showMessageDialog(frame, "I win!");
        } else {
            String newQuestion = JOptionPane.showInputDialog(frame, "You win!\nWhat question would help me guess better next time?");
            String newAnswer;
            do {
                newAnswer = JOptionPane.showInputDialog(frame, "What is the correct answer for that question?");
                newAnswer = newAnswer.toLowerCase();
                if (!newAnswer.equals("yes") && !newAnswer.equals("no")) {
                    JOptionPane.showMessageDialog(frame, "Please enter either 'yes' or 'no'.");
                }
            } while (!newAnswer.equals("yes") && !newAnswer.equals("no"));

            // modifying current node to become an internal node
            if (current != null) {
                String previousData = current.data;
                current.data = newQuestion;
                current.yes = new Node(newAnswer.equals("yes") ? current.data : newAnswer);
                current.no = new Node(newAnswer.equals("no") ? current.data : newAnswer);
            } else {
                current = new Node(newQuestion);
                current.yes = new Node(newAnswer.equals("yes") ? newQuestion : newAnswer);
                current.no = new Node(newAnswer.equals("no") ? newQuestion : newAnswer);
            }
        }
        saveTree();
    }

    // loading the tree from the file
    private Node loadTree() {
        try {
            FileInputStream fileIn = new FileInputStream(QUESTION_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Node tree = (Node) objectIn.readObject();
            objectIn.close();
            return tree;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    // saving the tree to the file
    private void saveTree() {
        try {
            FileOutputStream fileOut = new FileOutputStream(QUESTION_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(current); // Save the root node of the tree
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // constructing the default tree
    private Node constructTree() {
        Node root = new Node("Is it a flower?");
        root.yes = new Node("Is it red?");
        root.no = new Node("Is it a fruit?");
        root.yes.yes = new Node("Does it have thorns?");
        root.yes.no = new Node("Does it have a pleasant scent?");
        root.yes.yes.yes = new Node("Is it a rose?");
        root.yes.yes.no = new Node("Is it a poppy?");
        root.yes.no.yes = new Node("Is it yellow?");
        root.yes.no.no = new Node("Does it have petals with multiple colors?");
        root.yes.no.yes.yes = new Node("Is it a sunflower?");
        root.yes.no.yes.no = new Node("Is it an orchid?");
        root.yes.no.no.yes = new Node("Is it a tulip?");
        root.yes.no.no.no = new Node("Is it a daisy?");
        root.no.yes = new Node("Is it sweet?");
        root.no.no = new Node("Is it green?");
        root.no.yes.yes = new Node("Do you eat it raw?");
        root.no.yes.no = new Node("Is it used in cooking?");
        root.no.yes.yes.yes = new Node("Is it a strawberry?");
        root.no.yes.yes.no = new Node("Is it a cherry?");
        root.no.yes.no.yes = new Node("Is it an apple?");
        root.no.yes.no.no = new Node("Is it a pear?");
        root.no.no.yes = new Node("Is it sour?");
        root.no.no.no = new Node("Is it round?");
        root.no.no.yes.yes = new Node("Is it a lemon?");
        root.no.no.yes.no = new Node("Is it an orange?");
        root.no.no.no.yes = new Node("Is it a kiwi?");
        root.no.no.no.no = new Node("Is it a lime?");
        return root;
    }
    //displaying the GUI
    public void display() {
        frame.setVisible(true);
    }

   // starting the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TwentyQuestions game = new TwentyQuestions();
                game.display();
            }
        });
    }
}
