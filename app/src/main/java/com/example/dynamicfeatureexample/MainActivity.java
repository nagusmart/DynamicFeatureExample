package com.example.dynamicfeatureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    SplitInstallManager manager;

    TextView textView;

    Integer mySessionID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.install_new_apk);

        manager = SplitInstallManagerFactory.create(this);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager.getInstalledModules().contains("dynamicfeature")) {
                    SplitInstallRequest request =
                            SplitInstallRequest
                                    .newBuilder()
                                    .addModule("dynamicfeature")
                                    .build();

                    manager.startInstall(request)
                            .addOnSuccessListener(new OnSuccessListener<Integer>() {
                                @Override
                                public void onSuccess(Integer sessionId) {
                                    mySessionID = sessionId;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception exception) {
                                }
                            });

                }
            }
        });



        SplitInstallStateUpdatedListener listener =
                new SplitInstallStateUpdatedListener() {
                    @Override
                    public void onStateUpdate(SplitInstallSessionState state) {

                        if (state.sessionId() == mySessionID) {
                            switch (state.status()) {
                                case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                                    // Large module requires user permission
                                    break;

                                case SplitInstallSessionStatus.DOWNLOADING:
                                    // The module is being downloaded
                                    break;

                                case SplitInstallSessionStatus.INSTALLING:
                                    Toast.makeText(MainActivity.this,"installing",Toast.LENGTH_SHORT).show();
                                    break;

                                case SplitInstallSessionStatus.DOWNLOADED:
                                    // The module download is complete
                                    break;

                                case SplitInstallSessionStatus.INSTALLED:
                                    Toast.makeText(MainActivity.this,"Installed",Toast.LENGTH_SHORT).show();
                                    break;

                                case SplitInstallSessionStatus.CANCELED:
                                    // The user cancelled the download
                                    break;

                                case SplitInstallSessionStatus.PENDING:
                                    // The installation is deferred
                                    break;

                                case SplitInstallSessionStatus.FAILED:
                                    // The installation failed
                            }
                        }
                    }
                };


        manager.registerListener(listener);
    }

}
