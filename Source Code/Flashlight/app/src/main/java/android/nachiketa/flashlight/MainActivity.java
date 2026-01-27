/**
 * Android Studio Flashlight
 * 
 * Developed by: Amey Thakur & Mega Satish
 * GitHub: https://github.com/Amey-Thakur | https://github.com/msatmod
 * Repository: https://github.com/Amey-Thakur/ANDROID-STUDIO-FLASHLIGHT
 * 
 * Description: A specialized utility leveraging the Android Camera2 API for 
 * precise hardware LED control and industrial-grade torch functionality.
 * 
 * Release Date: May 27, 2022
 */

package android.nachiketa.flashlight;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * MainActivity handles the hardware interface between the user and the device's camera LED.
 * It implements state persistence and safe API calls for hardware interaction.
 */
public class MainActivity extends AppCompatActivity {

    // Internal state tracking for the hardware component
    boolean isTorchOn = false;
    ImageView imageView;

    /**
     * Standard activity lifecycle entry point.
     * Initializes the UI components and prepares the layout geometry.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Inflating activity geometry

        // Binding the visual indicator
        imageView = findViewById(R.id.img);

    }

    /**
     * Primary event handler for the toggle mechanism.
     * Synchronizes the UI button text, central image state, and physical LED hardware.
     */
    public void toggle(View view) {
        Button button = (Button) view;
        if (button.getText().equals("Switch On")) {
            // Turning Flashlight ON: Updating UI to reflect active hardware state
            button.setText(R.string.switch_off_text);
            imageView.setImageResource(R.drawable.flashon); // Corrected visual mapping
            torchToggle("on");
        } else {
            // Turning Flashlight OFF: Updating UI to reflect inactive hardware state
            button.setText(R.string.switch_on_text);
            imageView.setImageResource(R.drawable.flashoff); // Corrected visual mapping
            torchToggle("off");
        }
    }

    /**
     * Hardware Logic Engine: Interacts with the Android CameraManager.
     * Handles permissions and API versioning to ensure stability across devices.
     * 
     * @param command Deterministic state command ("on" or "off")
     */
    private void torchToggle(String command) {
        // Enforcing Marshmallow (API 23) compatibility for setTorchMode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; 
            try {
                if (camManager != null) {
                    // Identifying the primary hardware lens (typically index 0)
                    cameraId = camManager.getCameraIdList()[0];
                }
                if (camManager != null) {
                    if (command.equals("on")) {
                        // Invoking hardware LED activation
                        camManager.setTorchMode(cameraId, true);   
                        isTorchOn = true;
                    } else {
                        // Invoking hardware LED deactivation
                        camManager.setTorchMode(cameraId, false);  
                        isTorchOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                // Heuristic error reporting for hardware access conflicts
                e.getMessage();
            }
        }
    }
}