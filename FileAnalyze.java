import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Font;

public class FileAnalyze extends JFrame {

    private final JTextArea resultTextArea;

    public FileAnalyze() {
        super("Анализ файла");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        createMenuBar();
        createToolBar();

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        Font font = new Font("Monospaced", Font.PLAIN, 20);
        resultTextArea.setFont(font);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openMenuItem = new JMenuItem("Открыть");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        JButton chooseFileButton = new JButton(new ImageIcon("path/to/open.png")); 
        chooseFileButton.setToolTipText("Выбрать файл");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        toolBar.add(chooseFileButton);
        add(toolBar, BorderLayout.NORTH);
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));

        int result = fileChooser.showOpenDialog(FileAnalyze.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            analyzeFile(selectedFile);
        }
    }

    private void analyzeFile(File file) {
        resultTextArea.setText("");

        Node head = null;
        Node tail = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                int length = line.length();
                Node node = new Node(length);
                if (head == null) {
                    head = node;
                    tail = node;
                } else {
                    tail.next = node;
                    node.prev = tail;
                    tail = node;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node current = head;
        while (current != null) {
            resultTextArea.append("Длина строки: " + current.data + "\n");
            current = current.next;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileAnalyze::new);
    }

    static class Node {
        int data;
        Node prev;
        Node next;

        public Node(int d) {
            this.data = d;
            this.prev = null;
            this.next = null;
        }
    }
}
