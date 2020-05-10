package ui;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AlgorithmsRace;
import thread.AlgorithmsThread;
import thread.ChronometerThread;
import thread.PrepareRaceThread;

public class AlgorithmsRaceGUI {
	
	private AlgorithmsRace algorithmsRace;
	private boolean isRunning;
	private String time;
	private Stage window;
	public AlgorithmsRaceGUI(AlgorithmsRace ar,Stage win) {
		window = win;
		algorithmsRace =ar;
		isRunning = false;
		time = " ";
	}
	
	public void initialize() throws IOException {
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				isRunning = false;
				System.out.println("Closing the window!");
			}
		});
	}
	
	//Main
	@FXML
	private BorderPane mainPane;
	
	//Start
    @FXML
    private TextField textN;

    @FXML
    private CheckBox addSelect;

    @FXML
    private CheckBox searchSelect;

    @FXML
    private CheckBox delateSelect;

    @FXML
    private CheckBox iterativeSelect;

    @FXML
    private CheckBox recursiveSelect;

    @FXML
    private Button btnRun;
    
    @FXML
    private Button btnPrepare;

    @FXML
    private Label arrayListTime;

    @FXML
    private Label linkedListTime;

    @FXML
    private Label abbTime;

    @FXML
    private Label timer;

    @FXML
    private Circle bigCurcule;

    @FXML
    private Circle littleCircule;

    @FXML
    private ProgressBar loadingProgress;

    @FXML
    private Label labelLoading;
    

    @FXML
    void iterativeSelect(ActionEvent event) {
    	if(iterativeSelect.isSelected()) {
    		recursiveSelect.setSelected(false);
    	}
    	buttons();
    }

    @FXML
    void recursiveSelect(ActionEvent event) {
    	if(recursiveSelect.isSelected()) {
    		iterativeSelect.setSelected(false);
    	}
    	buttons();
    }

    @FXML
    void addSelect(ActionEvent event) {
    	if(addSelect.isSelected()) {
    		searchSelect.setSelected(false);
    		delateSelect.setSelected(false);
    	}
    	buttons();
    }

    @FXML
    void delateSelect(ActionEvent event) {
    	if(delateSelect.isSelected()) {
    		searchSelect.setSelected(false);
    		addSelect.setSelected(false);
    	}
    	buttons();
    }

    @FXML
    void searchSelect(ActionEvent event) {
    	if(searchSelect.isSelected()) {
    		delateSelect.setSelected(false);
    		addSelect.setSelected(false);
    	}
    	buttons();
    }
    
    @FXML
    void btnPrepare(ActionEvent event) throws InterruptedException {
    	try {
    		
        	
        	PrepareRaceThread preparing = new PrepareRaceThread(algorithmsRace,addSelect.isSelected()?false:true,Integer.parseInt(textN.getText()));
        	
        	/////////////////////////////////////////////////////////////////////////////////////////////////////
        	isRunning=true;
        	btnPrepare.setDisable(true);
        	addSelect.setDisable(true);
        	searchSelect.setDisable(true);
        	delateSelect.setDisable(true);
        	iterativeSelect.setDisable(true);
        	recursiveSelect.setDisable(true);
        	//////////////////////////////////////////////////////////
        	preparing.start();
        	/*new Thread() {
        		public void run() {
        			while(isRunning) {
        				Platform.runLater(new Thread() {
            				public void run() {
            					updateProgress();
            				}
            			});
					}
        			try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        	}.start();*/
        	preparing.join();
        	isRunning=false;
        	btnRun.setVisible(true);
        	
    	}catch(NumberFormatException e) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Error in format");
    		alert.setHeaderText("Please type only numbers");
    	}
    	
    }
    
    @FXML
    void btnRun(ActionEvent event) throws InterruptedException {
    	btnRun.setDisable(true);
    	int algorithm = addSelect.isSelected()?1:searchSelect.isSelected()?2:3;
    	int mode = iterativeSelect.isSelected()?1:2;
    	long n = Long.parseLong(textN.getText());
    	ChronometerThread timer = new ChronometerThread(this);
    	AlgorithmsThread arrayListThread = new AlgorithmsThread(this,algorithmsRace,algorithm,mode,1,n);
    	AlgorithmsThread linkedListThread = new AlgorithmsThread(this,algorithmsRace,algorithm,mode,2,n);
    	AlgorithmsThread binaryTreeThread = new AlgorithmsThread(this,algorithmsRace,algorithm,mode,3,n);
    	isRunning = true;
    	arrayListThread.start();
    	linkedListThread.start();
    	binaryTreeThread.start();
    	timer.start();
    	arrayListThread.join();
    	linkedListThread.join();
    	binaryTreeThread.join();
    	isRunning = false;
    	
    	//////////////////////////////////////////////////////////
    	btnRun.setVisible(false);
    	btnRun.setDisable(false);
    	btnPrepare.setDisable(false);
    	addSelect.setDisable(false);
    	searchSelect.setDisable(false);
    	delateSelect.setDisable(false);
    	iterativeSelect.setDisable(false);
    	recursiveSelect.setDisable(false);
    }
    
    public void buttons() {
  
    	if(iterativeSelect.isSelected() || recursiveSelect.isSelected()) {
    		if(addSelect.isSelected()||searchSelect.isSelected()||delateSelect.isSelected()) {
    			btnPrepare.setDisable(false);
    		}else {
    			btnPrepare.setDisable(true);
    		}
    	}else {
    		btnPrepare.setDisable(true);
    	}
    }
    
    public void updateTime(int m,int s,int cs) {
    	String time="";
    	if(cs<9) {
    		time = "0"+ Integer.toString(cs);
    	}else {
    		time = Integer.toString(cs);
    	}
    	if(s<9) {
    		time = "0"+ Integer.toString(s)+":"+time;
    	}else {
    		time = Integer.toString(s)+":"+time;
    	}
    	if(m<9) {
    		time = "0"+ Integer.toString(m)+":"+time;
    	}else {
    		time = Integer.toString(m)+":"+time;
    	}
    	this.time=time;
    }
    
    public void updateTimer() {
    	timer.setText(time);
    }
    
    public void updateTimerAL() {
    	arrayListTime.setText(time);
	}
    
    public void updateTimerLE() {
    	linkedListTime.setText(time);
    }
    
    public void updateTimerAbb() {
    	abbTime.setText(time);
    }
    
    public void updateProgress() {
    	loadingProgress.setProgress(algorithmsRace.getProgress());
    	
    }

	public boolean isRunning() {
		return isRunning;
	}
    
}
