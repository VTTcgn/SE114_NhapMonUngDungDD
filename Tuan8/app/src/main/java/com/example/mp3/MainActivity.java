package com.example.mp3;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 1;
    private Button btnUpload, btnPlayPause;
    private TextView tvSongName, tvDuration;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private String songPath;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpload = findViewById(R.id.btnUpload);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        tvSongName = findViewById(R.id.tvSongName);
        tvDuration = findViewById(R.id.tvDuration);
        seekBar = findViewById(R.id.seekBar);

        btnUpload.setOnClickListener(v -> openFileChooser());

        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    btnPlayPause.setText("Play");
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setText("Pause");
                }
                isPlaying = !isPlaying;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(Intent.createChooser(intent, "Choose MP3"), PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            songPath = getRealPathFromURI(uri);

            // Lấy tên file MP3
            String fileName = uri.getLastPathSegment();
            if (fileName != null) {
                // Kiểm tra xem fileName có chứa dấu '.' hay không
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex != -1) {
                    // Tách tên bài hát từ tên file
                    String songTitle = fileName.substring(0, dotIndex); // Loại bỏ đuôi file
                    tvSongName.setText(songTitle);
                } else {
                    // Nếu không có đuôi, chỉ cần hiển thị tên file
                    tvSongName.setText(fileName);
                }
            }

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();
            isPlaying = true;
            btnPlayPause.setText("Pause");

            seekBar.setMax(mediaPlayer.getDuration());
            tvDuration.setText(formatDuration(mediaPlayer.getDuration()));

            mediaPlayer.setOnCompletionListener(mp -> {
                seekBar.setProgress(0);
                tvDuration.setText(formatDuration(0));
                btnPlayPause.setText("Play");
                isPlaying = false;
            });

            new Thread(() -> {
                while (mediaPlayer != null) {
                    try {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}