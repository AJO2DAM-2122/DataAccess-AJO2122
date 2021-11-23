package u3.aajaor2122.com;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 *  CONTROLLER class - related to every aspect of the graphical interface
 * @author Aarón Jamet Orgiles - 2DAMU
 */

public class MainForm {
    private JPanel mainFormPanel;
    private JTabbedPane Enrollment;
    private JPanel Students;
    private JPanel Enrollments;
    private JLabel labelFirstName;
    private JTextField textFirstName;
    private JLabel labelLastName;
    private JTextField textIDCard;
    private JTextField textLastName;
    private JTextField textEmail;
    private JTextField textPhone;
    private JLabel labelIDCard;
    private JLabel labelEmail;
    private JLabel labelPhone;
    private JButton buttonAdd;
    private JComboBox comboBoxStudent;
    private JComboBox comboBoxCourse;
    private JButton buttonEnroll;
    private JComboBox cbStudentReports;
    private JTextArea textScoresResult;
    private JButton buttonPrint;
    private JButton buttonImportXML;
    private JLabel labelResult;
    private JPanel Reports;
    private JPanel Utilities;

    // Constructor for the graphical interface or GUI
    public MainForm() {

        setUpStudentsUI();
        setUpEnrollmentsUI();
        setUpStudentReportsUI();

        buttonEnroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int studentId = (Integer)comboBoxStudent.getSelectedItem();
                    int courseId = ((Course)comboBoxCourse.getSelectedItem()).getCode();

                    MyVTInstituteDB.insertEnrollment(studentId, courseId);
                    resultSuccess("Student has been enrolled into a new course.");
                } catch (Exception ex) {
                    reportError(ex);
                }
            }
        });

        buttonPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int studentId = (Integer)cbStudentReports.getSelectedItem();
                    String result = MyVTInstituteDB.printStudentString(studentId);

                    textScoresResult.setText(result);

                    // Llamar a JFILECHOOSER para elegir ruta donde guardar fichero
                } catch (Exception ex) {
                    reportError(ex);
                }

            }
        });
    }

    public void setUpStudentsUI() {
        // Add button listener; inserts new students into the database
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!textFirstName.getText().isEmpty() && !textLastName.getText().isEmpty() &&
                            !textIDCard.getText().isEmpty()) {
                        String name = textFirstName.getText();
                        String surname = textLastName.getText();
                        int id = Integer.valueOf(textIDCard.getText());
                        String email = textEmail.getText();
                        String phone = textPhone.getText();

                        MyVTInstituteDB.insertStudent(name, surname, id, email, phone);

                        setUpEnrollmentsUI();
                        setUpStudentReportsUI();
                        resultSuccess("Student inserted into the DB");
                        // refreshStudentBox();
                    } else {
                        labelResult.setText("Name, surname, and Id are REQUIRED");
                    }
                } catch (Exception ex) {
                    reportError(ex);
                }
            }
        });
    }

    public void setUpEnrollmentsUI() {
        try {
            comboBoxStudent.removeAllItems();
            comboBoxCourse.removeAllItems();

            List<Integer> allStudentsIds = MyVTInstituteDB.getAllStudentIds();
            for (Integer id: allStudentsIds) {
                comboBoxStudent.addItem(id);
            }

            List<Course> allCourses = MyVTInstituteDB.getAllCourses();
            for (Course course: allCourses) {
                comboBoxCourse.addItem(course);
            }

        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public void setUpStudentReportsUI() {
        try {
            cbStudentReports.removeAllItems();

            List<Integer> allStudentsIds = MyVTInstituteDB.getAllStudentIds();
            for (Integer id: allStudentsIds) {
                cbStudentReports.addItem(id);
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public void reportError(Exception ex) {
        JOptionPane.showMessageDialog(null, ex);

        ex.printStackTrace();
    }

    public void resultSuccess(String message) {

        JOptionPane.showMessageDialog(null, message);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("VTInstitute-AJO2122");
        frame.setContentPane(new MainForm().mainFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(380, 420);
        frame.setVisible(true);


    }

}
