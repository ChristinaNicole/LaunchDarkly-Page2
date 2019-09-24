/*
 * Simple JavaFX application with a feature, prime number generator, that can be disabled.
 * 
 * Author: Christina Cannavale
 * Page 2 project
 * 
 */

/*
 * Import necessary libraries
 */

import javafx.application.Application;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/*
 * LaunchDarkly feature flag
 */
import com.launchdarkly.client.*;

import java.util.Properties;
import java.util.Set;

/*
 * Here we go...
 */
public class LaunchDarklyPage2 extends Application {

	LDClient ldClient;

	public static void main(String[] args) {
		launch(args); // Run GUI application
	}

	@Override
	public void start(Stage mainStage) throws Exception {

		/*
		 * User information
		 */
		String userName = System.getProperty("user.name");
		String userEmail = userName + "@none.non";

		/*
		 * Create LaunchDarly client
		 */
		ldClient = new LDClient("sdk-c0c0a7c3-b20c-49a6-a9c2-f040a749258b");
		LDUser ldUser = new LDUser(userEmail);
		boolean showFeature = ldClient.boolVariation("feature-flag-1", ldUser, true);

		mainStage.setTitle("Generate sequences");
		/*
		 * The application's space
		 */
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(50));
		VBox vb = new VBox();
		vb.setSpacing(5);
		bp.setCenter(vb);

		/*
		 * Set choices
		 */

		ToggleGroup tg = new ToggleGroup();

		RadioButton rb1 = new RadioButton("Squares");
		rb1.setToggleGroup(tg);
		vb.getChildren().add(rb1);

		RadioButton rb2 = new RadioButton("Cubes");
		rb2.setToggleGroup(tg);
		vb.getChildren().add(rb2);

		/*
		 * New feature!
		 */

		if (showFeature) {
			RadioButton rb3 = new RadioButton("Fibonacci");
			rb3.setToggleGroup(tg);
			vb.getChildren().add(rb3);
		}

		/*
		 * Number of elements to generate
		 */

		TextField tf = new TextField();
		tf.setPromptText("Number of iterations?");
		tf.getText();
		vb.getChildren().add(tf);

		/*
		 * Return results here
		 */

		TextField rtf = new TextField();
		rtf.setDisable(true);
		vb.getChildren().add(rtf);

		/*
		 * Start processing
		 */

		Button b = new Button("Go");
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				go(rtf, (RadioButton) tg.getSelectedToggle(), tf.getText());
				tf.clear();
			}
		});
		vb.getChildren().add(b);

		/*
		 * Show and tell...
		 */

		Scene s = new Scene(bp, 750, 250);
		mainStage.setScene(s);
		mainStage.show();
	}

	@Override
	public void stop() throws Exception {
		ldClient.close();
		Properties prop = System.getProperties();
	}

	/*
	 * Go button clicked. Determine choices and process
	 */
	private void go(TextField field, RadioButton choice, String option) {
		String rtn = "";
		int values;
		try {
			if (choice != null) {
				values = Integer.parseInt(option);
				if (values > 20)
					rtn += "Max of 20 iterations, please. ";
				else
					switch (choice.getText()) {
					case "Squares":
						rtn += squares(values);
						break;
					case "Cubes":
						rtn += cubes(values);
						break;
					case "Fibonacci":
						rtn += fibonacci(values);
						break;
					default:
						rtn += "No choice made. "; // not really necessary, but... nice to have.
					}
			} else
				rtn += "No choice made. ";

		} catch (Exception e) {
			rtn += "Integer value, please. ";
			values = 0;
		}
		field.setText(rtn);
	}

	/*
	 * Calculate squares
	 */
	private String squares(int max) {
		String rtn = "1";
		for (int i = 2; i <= max; i++)
			rtn += ", " + (i * i);
		return rtn;
	}

	/*
	 * Calculate cubes
	 */
	private String cubes(int max) {
		String rtn = "1";
		for (int i = 2; i <= max; i++)
			rtn += ", " + (i * i * i);
		return rtn;
	}

	/*
	 * Calculate fibonacci
	 */
	private String fibonacci(int max) {
		String rtn = "1";
		rtn = calculateFibonacci(0, 1, 1, max);
		return rtn;
	}

	private String calculateFibonacci(int v1, int v2, int calls, int max) {
		String rtn;
		if (calls >= max)
			return rtn = Integer.toString(v1 + v2);
		int value = v1 + v2;
		rtn = value + ", " + calculateFibonacci(v2, value, calls + 1, max);
		return rtn;
	}

}
