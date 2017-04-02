package com.example.android.makeathon;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    List<Student> dataSource = new ArrayList<>();
    RecyclerView recyclerView;
    ListAdapter adapter;
    TextView heading;
    SwipeRefreshLayout swipeRefreshLayout;


    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fragment1, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        heading = (TextView) v.findViewById(R.id.heading);
        heading.setText("Presentees List");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataSource.clear();
                new RetrieveTask().execute();

            }
        });

        new RetrieveTask().execute();
        return v;
    }

    class RetrieveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect("192.168.5.3", 21);
                ftpClient.enterLocalPassiveMode();
                ftpClient.login("FTP_User", "ravi1998");

                ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);

                InputStream inStream = ftpClient.retrieveFileStream("record03.xls");

                ftpClient.disconnect();
                Workbook wb = Workbook.getWorkbook(inStream);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();

                for (int i = 1; i < row; i++) {
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
                inStream.close();


            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "ioexception", Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "exception", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ListAdapter(getContext(), dataSource);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);


        }
    }
}
