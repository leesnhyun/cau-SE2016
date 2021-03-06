package model;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by starl1ght on 2016-05-08.
 */
public class FileModelTest {
    private FileModel testFileModel;
    private ArrayList<String> testStringArrayList;
    @Before
    public void setupTest(){
        testFileModel = new FileModel();
    }
    @Test
    public void fileReadTest() throws FileNotFoundException{
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        ArrayList<String>
        testStringArrayList = new ArrayList<>();
        testStringArrayList.add("a");
        testStringArrayList.add("b");
        testStringArrayList.add("c");
        testStringArrayList.add("d");
        assertEquals(testFileModel.getLineArrayList().size(), testStringArrayList.size());
        for(int i = 0; i < testStringArrayList.size(); i++){
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testStringArrayList.get(i)));
        }
    }
    @Test(expected = FileNotFoundException.class)
    public void fileReadTest_FileNotFount() throws FileNotFoundException{
        testFileModel.readFile("AAsdfdsfsdfdsvevsvxcvcxvxcvcxvcx.txt");
    }

    @Test
    public void updateArrayListTest() {
        testFileModel.updateArrayList("z\nx\ny\ne");
        testStringArrayList = new ArrayList<>();
        testStringArrayList.add("z");
        testStringArrayList.add("x");
        testStringArrayList.add("y");
        testStringArrayList.add("e");
        for (int i = 0; i < testStringArrayList.size(); i++) {
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testStringArrayList.get(i)));
        }
    }
    @Test
    public void updateArrayListTest_emptyString() {
        testFileModel.updateArrayList("");
        testStringArrayList = new ArrayList<>();
        testStringArrayList.add("");
        for (int i = 0; i < testStringArrayList.size(); i++) {
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testStringArrayList.get(i)));
        }
    }
    @Test
    public void updateArrayListTest_null() {
        testFileModel.updateArrayList(null);
        testStringArrayList = new ArrayList<>();
        testStringArrayList.add("");
        for (int i = 0; i < testStringArrayList.size(); i++) {
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testStringArrayList.get(i)));
        }
    }
    @Test
    public void isFileExistTest_Exist()throws FileNotFoundException{
        assertFalse(testFileModel.isFileExist());
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        assertTrue(testFileModel.isFileExist());
    }
    @Test
    public void editedTest() throws FileNotFoundException{
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        assertFalse(testFileModel.getEdited());
        testFileModel.setEdited(true);
        assertTrue(testFileModel.getEdited());
        testFileModel.writeFile();
        assertFalse(testFileModel.getEdited());
    }
    @Test
    //미구현
    public void getStatusTest()
    {
        /*
        FileModel testFileModel = new FileModel();
        String testStatus = "Ready(No file is loaded)";
        //assertEquals(testFileModel.getStatus(), new ReadOnlyStringWrapper(testStatus));
        //assertTrue(testFileModel.readFile("A.txt"));
        //testStatus = "File Loaded Successfully";
        //assertEquals(testFileModel.getStatus(),new ReadOnlyStringWrapper(testStatus));
        //testFileModel.writeFile("AA.txt");
        testStatus = "File Written successfully";
        //assertEquals(testFileModel.getStatus(),new ReadOnlyStringWrapper(testStatus));
    */
    }

}