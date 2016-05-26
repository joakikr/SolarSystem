package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import application.OrbitalElement.ElementSize;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Controller implements Initializable, Commons {
	
	/**
	 * The date of measured orbital elements.
	 */
	private DateTime dateOfMeasure;
	
	/**
	 * The date of current Solarsystem view.
	 */
	private DateTime viewDate;
	
	/**
	 * All elements in Solarsystem.
	 */
	private List<OrbitalElement> solarSystem;
	
	/**
	 * All planets in Solarsystem.
	 */
	private List<OrbitalElement> planets;

	/**
	 * Show or hide the shortest path.
	 */
	private boolean showTSP;
	
	/**
	 * Invoker to redraw solarsystem and shortest path.
	 */
	private Timeline timeline;
	
	/**
	 * GUI Variables.
	 */
	@FXML private Pane root;
	@FXML private StackPane container;
	@FXML private Canvas canvas;
	@FXML private Slider dateRate;
	@FXML private Label date;
	@FXML private Button tsp;
	
	/**
	 * Create the solar system from a given inFile
	 * which contains the necessary orbital elements of 
	 * each planet/star.
	 */
	private void createSolarSystem(String inFile) {
		solarSystem = new ArrayList<OrbitalElement>();
		planets = new ArrayList<OrbitalElement>();

		try {
			File file = null;
		    URL res = getClass().getResource(inFile);
		    
		    // === File IO are handled different in a jar run ===
		    if (res.toString().startsWith("jar:")) {
		    	int read;
		    	byte[] bytes = new byte[1024];

		    	InputStream input = getClass().getResourceAsStream(inFile);
	            file = File.createTempFile("tempfile", ".tmp");
	            OutputStream out = new FileOutputStream(file);

	            while ((read = input.read(bytes)) != -1) {
	                out.write(bytes, 0, read);
	            }
	            
	            input.close();
	            out.close();
	            file.deleteOnExit();

	        // === All other runs ===
		    } else {
		        file = new File(res.getFile());
		    }
		    
		    Scanner in = new Scanner(file);
			
			// === Read the date of measured orbital elements ===
			dateOfMeasure = new DateTime(in.nextLine());
			
			// === Skip line describing variables ===
			in.nextLine();
			
			// === Read in data ===
			while(in.hasNextLine()) {
				// === type, name, size, d, N, i, w, a, e, M, n ===
				String[] line = in.nextLine().split(" ");

				// == Skip commented elements ===
				if(line[0].startsWith("//")) {
					continue;
				}
				
				// === Find size of element ===
				ElementSize size;
				switch(line[2]) {
				case "VL":
					size = ElementSize.VERY_LARGE;
					break;
				case "L":
					size = ElementSize.LARGE;
					break;
				case "M":
					size = ElementSize.MEDIUM;
					break;
				case "S":
					size = ElementSize.SMALL;
					break;
				default:
					size = ElementSize.MEDIUM;
				}
				
				// === Check for star ===
				if(line[0].equals("star")) {
					solarSystem.add(new Star(line[1], size));
					continue;
				}
				
				// === Add on planet ===
				Planet p = new Planet(line[1], 
						size, 
						Double.parseDouble(line[3]), 
						Double.parseDouble(line[4]), 
						Double.parseDouble(line[5]), 
						Double.parseDouble(line[6]), 
						Double.parseDouble(line[7]), 
						Double.parseDouble(line[8]),
						Double.parseDouble(line[9]));
				
				solarSystem.add(p);
				planets.add(p);
			}
			
			in.close();
			
		} catch(FileNotFoundException fnfe) {
			System.err.println("Could not find Orbital Elements file.");
			fnfe.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Could not create temp file.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void drawSolarSystem(GraphicsContext gc) {
        // === Draw on sun ===
        Star sun = (Star) solarSystem.get(0);
    	gc.drawImage(sun.icon, APP_WIDTH/2.0 - sun.iconSize/2.0, APP_HEIGHT/2.0 - sun.iconSize/2.0);
        
        // === Draw on planets ===
    	double distance = STAR_SPACING;
        for(int i = 1; i < solarSystem.size(); i++) {
        	OrbitalElement elem = solarSystem.get(i);
        	
        	// === Skip non planets ===
        	if(!(elem instanceof Planet)) continue;
        	
        	// === Normalize position and scale by distance from sun ===
        	Point3D point = elem.position.normalize();
        	double newX = point.getX() * distance; 
        	double newY = -1 * point.getY() * distance; // Mirror on y-axis to reflect results from www.theplanetstoday.com

        	// === Draw planetary orbit (Readjusted to circle, index steps away from unit circle) ===
        	gc.strokeOval(APP_WIDTH/2.0 - distance, APP_HEIGHT/2.0 - distance, 2*distance, 2*distance);
        	
        	// === Draw on planets name ===
    		gc.fillText(elem.name, APP_WIDTH/2.0 + newX, APP_HEIGHT/2.0 + newY + elem.iconSize * 0.75);
        		
        	// === Draw on planets icon ===
        	gc.drawImage(elem.icon, APP_WIDTH/2.0 + newX - elem.iconSize/2.0, APP_HEIGHT/2.0 + newY - elem.iconSize/2.0);
        
        	distance += PLANET_SPACING;
        }
	}
	
	private void drawDate(DateTime date, GraphicsContext gc) {
		((Label) root.lookup("#date")).setText("Date: " + date.toString(DateTimeFormat.forPattern("dd/MM/YYYY")));
	}

	private void drawBestPath(int[] bestPath, GraphicsContext gc, List<OrbitalElement> system) {
		for(int i = 0; i < bestPath.length - 1; i++) {
			Point3D one = system.get(bestPath[i]).position.normalize();
			Point3D two = system.get(bestPath[i + 1]).position.normalize();
			
			double dis1 = bestPath[i] * PLANET_SPACING + STAR_SPACING;
			double dis2 = bestPath[i + 1] * PLANET_SPACING + STAR_SPACING;
			
			double x1 = one.getX() * dis1;
			double y1 = one.getY() * dis1 * -1;
			double x2 = two.getX() * dis2;
			double y2 = two.getY() * dis2 * -1;
			
			gc.strokeLine(APP_WIDTH/2.0 + x1, APP_HEIGHT/2.0 + y1, APP_WIDTH/2.0 + x2, APP_HEIGHT/2.0 + y2);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// === Create Solarsystem ===
		createSolarSystem(ORBITAL_ELEMENTS_PATH);
		
		// === Style Graphics Context ===
		GraphicsContext gc = ((Canvas) root.lookup("#canvas")).getGraphicsContext2D();
        gc.setStroke(Color.web("#FF8109"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.OLDLACE);

		// === Setup ===
		Slider dateRate = ((Slider) root.lookup("#dateRate"));
		Button tsp = ((Button) root.lookup("#tsp"));
		tsp.setOnAction((event) -> {
			if(showTSP) {
				tsp.setText("SHOW PATH");
			} else {
				tsp.setText("HIDE PATH");
			}
			showTSP = !showTSP;
		});
		
		// === Update Solarsystem every CANVAS_UPDATE milliseconds ===
		viewDate = new DateTime();
		timeline = new Timeline(new KeyFrame(Duration.millis(CANVAS_UPDATE), event -> {
			// === Recalculate positions for given day ===
			for(OrbitalElement elem : solarSystem) {
				elem.calculatePostion(Utils.daysBetweenDates(dateOfMeasure, viewDate));
			}
			
			// === Clear graphics ===
			gc.clearRect(0, 0, APP_WIDTH, APP_HEIGHT);
			
			// === Show shortest path ===
			if(showTSP) {
				TSP solver = new TSP(planets.toArray(new OrbitalElement[planets.size()]));
				drawBestPath(solver.getOptimalPath(), gc, planets);
			}
			
			// === Show Solarsystem ===
			drawSolarSystem(gc);
			drawDate(viewDate, gc);
			
			// === Re-adjust days ===
			viewDate = viewDate.plusDays((int) dateRate.getValue());
			
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
}
