package com.alejandro_castilla.pacmangwt.client.gwt;

import com.alejandro_castilla.pacmangwt.client.GreetingService;
import com.alejandro_castilla.pacmangwt.client.GreetingServiceAsync;
import com.alejandro_castilla.pacmangwt.client.jre.Map;
import com.alejandro_castilla.pacmangwt.shared.FieldVerifier;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pacman_GWT implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	/**
	 * Widgets fields.
	 */
	
    private final String menuBarHolder = "menuBarContainer";
    private MenuBar menuBar;

    private final String canvasHolder = "canvasContainer";
    private final int canvasWidth = 800;
    private final int canvasHeight = 600;
    private Canvas canvas;
    private Context2d context2D;

	private Map maze = new Map();

    /**
     * Colors
     */

    private final CssColor mazeColor = CssColor.make("blue");
	private final CssColor canvasColor = CssColor.make("black");

    /**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
        createMenuBar();
        createCanvas();

        menuBar = createMenuBar();
        RootPanel.get(menuBarHolder).add(menuBar);
        RootPanel.get(canvasHolder).add(canvas);

        drawMaze();


		
		/**
         *  Example code. I'm using this to learn how to work with GWT.
         */
		
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
//		RootPanel.get("nameFieldContainer").add(nameField);
//		RootPanel.get("sendButtonContainer").add(sendButton);
//		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result);
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}

    private MenuBar createMenuBar() {
        Command menuBarHandler = new Command() {
            @Override
            public void execute() {
                Window.alert("Has pulsado una opción del menú.");
            }
        };

        MenuBar gameBar = new MenuBar(true);
        gameBar.addItem("Nuevo", menuBarHandler);
        gameBar.addItem("Pausar", menuBarHandler);

        MenuBar helpBar = new MenuBar(true);
        helpBar.addItem("Acerca de", menuBarHandler);

        MenuBar rootMenuBar = new MenuBar();
        rootMenuBar.addItem("Juego", gameBar);
        rootMenuBar.addItem("Ayuda", helpBar);

        return rootMenuBar;
    }

    private void createCanvas() {
        canvas = canvas.createIfSupported();

        canvas.setWidth(canvasWidth + "px");
        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);
        canvas.setCoordinateSpaceHeight(canvasHeight);
    }

    private void drawMaze() {
        int cellWidth = Math.min(canvasWidth / maze.getWidth(), canvasHeight / maze.getHeight());
		int x, y, offset = (canvasWidth - (maze.getWidth() * cellWidth)) / 2;

		context2D = canvas.getContext2d();
		context2D.beginPath();
		context2D.setFillStyle(canvasColor);
		context2D.fillRect(0, 0, canvasWidth, canvasWidth);
		context2D.closePath();

		for (x = 0 ; x < maze.getWidth() ; x++) {
			for (y = 0 ; y < maze.getHeight() ; y++) {
				switch (maze.getCellAt(x, y)) {
					case Map.RECTANGLE_UP:
						context2D.beginPath();
						context2D.setFillStyle(mazeColor);
						context2D.fillRect(offset + x*cellWidth, y*cellWidth, cellWidth, cellWidth/2);
						context2D.closePath();
						break;
					case Map.RECTANGLE_DOWN:
						context2D.beginPath();
						context2D.setFillStyle(mazeColor);
						context2D.fillRect(offset + x*cellWidth, y*cellWidth + cellWidth/2, cellWidth, cellWidth/2);
						context2D.closePath();
						break;
					case Map.RECTANGLE_LEFT:
						context2D.beginPath();
						context2D.setFillStyle(mazeColor);
						context2D.fillRect(offset + x*cellWidth, y*cellWidth, cellWidth/2, cellWidth);
						context2D.closePath();
						break;
					case Map.RECTANGLE_RIGHT:
						context2D.beginPath();
						context2D.setFillStyle(mazeColor);
						context2D.fillRect(offset + x*cellWidth + cellWidth/2, y*cellWidth, cellWidth/2, cellWidth);
						context2D.closePath();
						break;
					case Map.UPPER_LEFT_CORNER:
						context2D.beginPath();
						context2D.setFillStyle(mazeColor);
						context2D.moveTo(offset + x*cellWidth, y*cellWidth);
						context2D.lineTo(offset + x*cellWidth, y*cellWidth + cellWidth/2);
						context2D.lineTo(offset + x*cellWidth + cellWidth/2, y*cellWidth);
						context2D.moveTo(offset + x*cellWidth + cellWidth/2, y*cellWidth);
						context2D.lineTo(offset + x*cellWidth, y*cellWidth + cellWidth/2);
						context2D.fill();
						context2D.closePath();
						break;
					case Map.BOTTOM_LEFT_CORNER:
						//TODO Complete this
						break;
					case Map.UPPER_LEFT_BIG_CORNER:
						context2D.beginPath();
						context2D.setFillStyle(mazeColor);
						context2D.fillRect(offset + x*cellWidth, y*cellWidth, cellWidth, cellWidth);
						context2D.setFillStyle(canvasColor);
						context2D.moveTo(offset + x*cellWidth, y*cellWidth);
						context2D.lineTo(offset + x*cellWidth, y*cellWidth + cellWidth/2);
						context2D.lineTo(offset + x*cellWidth + cellWidth/2, y*cellWidth);
						context2D.moveTo(offset + x*cellWidth + cellWidth/2, y*cellWidth);
						context2D.lineTo(offset + x*cellWidth, y*cellWidth + cellWidth/2);
						context2D.fill();
						context2D.closePath();
						break;


				}
			}
		}



    }

}
