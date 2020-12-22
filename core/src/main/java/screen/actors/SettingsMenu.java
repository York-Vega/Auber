package screen.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tools.Controller;

public class SettingsMenu extends Menu {

    TextButton acceptButton = new TextButton("Accept", skin);
    TextButton resetButton = new TextButton("Reset", skin);
    TextField upTextField = new TextField("UP", skin);
    TextField downTextField = new TextField("DOWN", skin);
    TextField leftTextField = new TextField("LEFT", skin);
    TextField rightTextField = new TextField("RIGHT", skin);
    Label upLabel = new Label("Up", skin);
    Label downLabel = new Label("Down", skin);
    Label leftLabel = new Label("Left", skin);
    Label rightLabel = new Label("Right", skin);
    Label settingsLabel = new Label("Settings", skin);

    /**
     * The ingame settings menu.
     */
    public SettingsMenu() {
        super("Settings");

        acceptButton.setName("Accept");
        resetButton.setName("Reset");
        upTextField.setName("UpTF");
        downTextField.setName("DownTF");
        leftTextField.setName("LeftTF");
        rightTextField.setName("RightTF");
        upLabel.setName("UpLabel");
        downLabel.setName("DownLabel");
        leftLabel.setName("LeftLabel");
        rightLabel.setName("RightLabel");
        settingsLabel.setName("SettingsLabel");

        window.add(acceptButton);
        window.add(resetButton);
        window.add(upTextField);
        window.add(downTextField);
        window.add(leftTextField);
        window.add(rightTextField);
        window.add(upLabel);
        window.add(downLabel);
        window.add(leftLabel);
        window.add(rightLabel);
        window.add(settingsLabel);

        window.findActor("UpLabel").setHeight(upLabel.getHeight() / 2);
        window.findActor("DownLabel").setHeight(downLabel.getHeight() / 2);
        window.findActor("LeftLabel").setHeight(leftLabel.getHeight() / 2);
        window.findActor("RightLabel").setHeight(rightLabel.getHeight() / 2);

        window.findActor("UpLabel").setWidth(upLabel.getHeight() / 2);
        window.findActor("DownLabel").setWidth(downLabel.getWidth() / 2);
        window.findActor("LeftLabel").setWidth(leftLabel.getWidth() / 2);
        window.findActor("RightLabel").setWidth(rightLabel.getWidth() / 2);

        window.findActor("Accept").setPosition(this.window.getWidth() / 2 
            - this.acceptButton.getWidth() / 2,
                this.window.getHeight() * 1 / 11 - this.acceptButton.getHeight() / 2);
                
        window.findActor("Reset").setPosition(this.window.getWidth() / 2 
            - this.resetButton.getWidth() / 2,
                this.window.getHeight() * 2 / 11 - this.resetButton.getHeight() / 2);

        window.findActor("UpTF").setPosition(this.window.getWidth() / 2 
            - this.upTextField.getWidth() / 2,
                this.window.getHeight() * 10 / 13 - this.upTextField.getHeight() / 2);

        window.findActor("DownTF").setPosition(this.window.getWidth() / 2 
            - this.downTextField.getWidth() / 2,
                this.window.getHeight() * 8 / 13 - this.downTextField.getHeight() / 2);

        window.findActor("LeftTF").setPosition(this.window.getWidth() / 2 
            - this.leftTextField.getWidth() / 2,
                this.window.getHeight() * 6 / 13 - this.leftTextField.getHeight() / 2);

        window.findActor("RightTF").setPosition(this.window.getWidth() / 2 
            - this.rightTextField.getWidth() / 2,
                this.window.getHeight() * 4 / 13 - this.rightTextField.getHeight() / 2);

        window.findActor("UpLabel").setPosition(10, this.window.getHeight() 
            * 11 / 13 - this.upLabel.getHeight() / 2);

        window.findActor("DownLabel").setPosition(10,
                this.window.getHeight() * 9 / 13 - this.downLabel.getHeight() / 2);

        window.findActor("LeftLabel").setPosition(10,
                this.window.getHeight() * 7 / 13 - this.leftLabel.getHeight() / 2);

        window.findActor("RightLabel").setPosition(10,
                this.window.getHeight() * 5 / 13 - this.rightLabel.getHeight() / 2);

        window.findActor("SettingsLabel").setPosition(this.window.getWidth() / 2 
            - this.settingsLabel.getWidth() / 2,
                this.window.getHeight() * 12 / 13 - this.settingsLabel.getHeight() / 2);

    }

    public Window settingsWindow() {
        return this.window;
    }

    @Override
    public void show() {
        super.show();

        ((TextField) window.findActor("UpTF")).setText(Controller.up());
        ((TextField) window.findActor("DownTF")).setText(Controller.down());
        ((TextField) window.findActor("LeftTF")).setText(Controller.left());
        ((TextField) window.findActor("RightTF")).setText(Controller.right());

        // Event listeners for buttons
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeControlls();

                paused = false;
                SettingsMenu.super.hide();
            }
        });

        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((TextField) window.findActor("UpTF")).setText("Up");
                ((TextField) window.findActor("DownTF")).setText("Down");
                ((TextField) window.findActor("LeftTF")).setText("Left");
                ((TextField) window.findActor("RightTF")).setText("Right");
            }
        });
    }

    public boolean resume() {
        return !paused;
    }

    /**
     * changes the controls of the player to the ones in the text fields NOTE: keys
     * must be spelt exactly how they are in the API e.g. capital letters at index 0
     */
    private void changeControlls() {
        String up = (String) ((TextField) window.findActor("UpTF")).getText();
        String down = (String) ((TextField) window.findActor("DownTF")).getText();
        String left = (String) ((TextField) window.findActor("LeftTF")).getText();
        String right = (String) ((TextField) window.findActor("RightTF")).getText();

        Controller.changeControls(up, down, left, right);
    }

}
