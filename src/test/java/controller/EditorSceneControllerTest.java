package controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import mockInterface.FileDialogInterface;
import model.FileManager;
import model.FileManagerInterface;

import model.Line;
import model.LineInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import utils.TestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;


/**
 * Created by SH on 2016-06-04.
 */

public class EditorSceneControllerTest extends ApplicationTest {

    private Stage s;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(() -> new Stage());
    }

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();
    }

    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void saveTest() {
        //EditorSceneController ctrr = new EditorSceneController();
        //ctrr.useSaveActionMethod();
        //ctrr.testHelper(editor, btnFileSave, btnFileOpen, btnEdit);
    }

    @Test
    public void EditorSceneInitialButtonEnableTest(){
        Node[] buttons = { find("#btnFileOpen"), find("#btnFileSave"), find("#btnEdit") };

        for(Node node : buttons){
            verifyThat(node, NodeMatchers.isEnabled());
        }
    }
    @Test
    public void LoadSuccessTest() {
        //given :
        HighlightEditorInterface editor = find("#editor");

        //when :
        clickOn("#btnFileOpen");

        assertEquals(1, listTargetWindows().size());
        type(KeyCode.O, KeyCode.U, KeyCode.T, KeyCode.PERIOD, KeyCode.T, KeyCode.X, KeyCode.T, KeyCode.ENTER);

        //then :
        assertEquals(editor.getText(), FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left));

    }

    @Test
    public void SaveSuccessWithNoFileTest() {
        //when :
        clickOn("#btnEdit");
        clickOn("#editor");
        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        clickOn("#btnEdit");
        clickOn("#btnFileSave");
        //then :
        assertEquals(2, listTargetWindows().size());

    }


    @Test
    public void EditorSceneButtonLoadClickTest(){

        Node btnFileOpen = find("#btnFileOpen");
        HighlightEditorInterface editor = find("#editor");
        ListView<LineInterface> highlightListView = editor.getHighlightListView();

        clickOn(btnFileOpen);

        // 닫았는데 0이면 파일츄저가 안뜬거다.
        closeCurrentWindow();
        assertEquals(1, listTargetWindows().size());

        // OS 종속적인 Dialog 는 testFX로 테스팅이 불가능하다. 따라서 mock을 생성함.
        FileDialogInterface fileDialogMock = createMock(FileDialogInterface.class);
        expect(fileDialogMock.getPath()).andReturn( getClass().getResource("../test1-1.txt").getPath() );
        replay(fileDialogMock);

        WaitForAsyncUtils.waitForAsyncFx(5000, ()->{

            try {
                FileManager.getFileManagerInterface().loadFile(fileDialogMock.getPath(), FileManagerInterface.SideOfEditor.Left);
                String[] textContents = { "a", "b", "c", "d", "e", "c", "", "e", "d", "d", "e", "d", "c", "", "d", "d", "d", "d", "d" };
                ObservableList<LineInterface> items = highlightListView.getItems();

                for(int i=0; i<items.size(); i++) {
                    assertEquals(items.get(i).toString(), textContents[i]);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });

        verify(fileDialogMock);

    }

    @Test
    public void EditorSceneButtonEditClickTest(){

        Node[] buttons = { find("#btnFileOpen"), find("#btnFileSave"), find("#btnEdit"), find("#btnCompare") };
        clickOn(buttons[2]);
        WaitForAsyncUtils.waitForFxEvents();

        // Edit 버튼을 누르면 나머지는 비활성화 되어야 한다.
        verifyThat(buttons[0], NodeMatchers.isDisabled());
        verifyThat(buttons[1], NodeMatchers.isDisabled());

        // Edit 버튼을 누르면 textArea가 보여야 한다.
        Node textArea = find("#editor");
        verifyThat(textArea, NodeMatchers.isVisible());

        clickOn(buttons[2]);
    }

    @Test
    public void EditorSceneSyncEditorAndListViewTest(){

        Node btnEdit = find("#btnEdit");
        HighlightEditorInterface editor = find("#editor");
        ListView<LineInterface> highlightListView = editor.getHighlightListView();
        TextArea textArea = editor.getTextArea();

        clickOn(btnEdit); WaitForAsyncUtils.waitForFxEvents();

        clickOn(textArea).write("test!");
        clickOn(btnEdit);

        ObservableList<LineInterface> items = highlightListView.getItems();
        assertEquals(items.get(0).toString(), "test!");

    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();

        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Left);
        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Right);

        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}