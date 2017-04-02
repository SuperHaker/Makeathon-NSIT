package com.example.android.makeathon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {


    List<Student> dataSource = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        new RetrieveTask().execute();




    }

    class RetrieveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
//                File targetFile = new File(Environment.getExternalStorageDirectory() + File.separator + "Makeathon" + File.separator + "record.xls");
//                targetFile.mkdirs();
            FileOutputStream outputStream;

            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect("192.168.5.3", 21);
                ftpClient.enterLocalPassiveMode();
                ftpClient.login("FTP_User", "ravi1998");

                ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);

//                    outputStream = new FileOutputStream(targetFile);
                InputStream inStream = ftpClient.retrieveFileStream("record.xls");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();

                    }
                });
                ftpClient.disconnect();
//                    outputStream.close();
                Workbook wb = Workbook.getWorkbook(inStream);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

                for (int i = 0; i < row; i++) {
                    Student student = new Student();
                    Cell z = s.getCell(0, i);
                    student.id = z.getContents();
                    z = s.getCell(1, i);
                    student.name = z.getContents();
                    z = s.getCell(2, i);
                    student.branch = z.getContents();
                    z = s.getCell(3, i);
                    student.enrolNo = z.getContents();
                    dataSource.add(student);


                }




            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "ioexception", Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "exception", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ListAdapter(MainActivity.this, dataSource);
            recyclerView.setAdapter(adapter);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LogInActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
