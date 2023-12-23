package com.example.myapplication;

import static com.example.myapplication.MainActivity.yearlyExpense;
import static com.example.myapplication.MainActivity.yearlyIncome;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticsViewFragment extends Fragment {
    public static int index = Calendar.getInstance().get(Calendar.MONTH);
    private String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statisticsview, container, false);
        Spinner monthSpinner = view.findViewById(R.id.monthSpinner);
        TextView textView = view.findViewById(R.id.textView);

        try {
            FileInputStream fileIn = getContext().openFileInput("yearlyIncome.txt");
            if(fileIn !=null) {
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                yearlyIncome = (ArrayList<Integer>) objectIn.readObject();
                objectIn.close();
                fileIn.close();
            }
            fileIn = getContext().openFileInput("yearlyExpense.txt");
            if(fileIn !=null) {
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                yearlyExpense = (ArrayList<Integer>) objectIn.readObject();
                objectIn.close();
                fileIn.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        updateYearData();

        LineChartView formerLineChartView = view.findViewById(R.id.formerLineChartView);
        LineChartView latterLineChartView = view.findViewById(R.id.latterLineChartView);

        List<PointValue> formerIncomeValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { formerIncomeValues.add(new PointValue(i, yearlyIncome.get(i))); }
        List<PointValue> formerExpenseValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { formerExpenseValues.add(new PointValue(i, yearlyExpense.get(i))); }
        List<PointValue> latterIncomeValues = new ArrayList<>(); for (int i = 6; i < 12; i++) { latterIncomeValues.add(new PointValue(i - 6, yearlyIncome.get(i))); }
        List<PointValue> latterExpenseValues = new ArrayList<>(); for (int i = 6; i < 12; i++) { latterExpenseValues.add(new PointValue(i - 6, yearlyExpense.get(i))); }

        Line formerIncomeLine = new Line(formerIncomeValues).setColor(Color.RED).setCubic(false); formerIncomeLine.setShape(ValueShape.CIRCLE); formerIncomeLine.setPointRadius(4);
        Line formerExpenseLine = new Line(formerExpenseValues).setColor(Color.BLACK).setCubic(false); formerExpenseLine.setShape(ValueShape.CIRCLE); formerExpenseLine.setPointRadius(4);
        Line latterIncomeLine = new Line(latterIncomeValues).setColor(Color.RED).setCubic(false); latterIncomeLine.setShape(ValueShape.CIRCLE); latterIncomeLine.setPointRadius(4);
        Line latterExpenseLine = new Line(latterExpenseValues).setColor(Color.BLACK).setCubic(false); latterExpenseLine.setShape(ValueShape.CIRCLE); latterExpenseLine.setPointRadius(4);

        List<Line> formerLines = new ArrayList<>(); formerLines.add(formerIncomeLine); formerLines.add(formerExpenseLine);
        List<Line> latterLines = new ArrayList<>(); latterLines.add(latterIncomeLine); latterLines.add(latterExpenseLine);

        List<AxisValue> formerAxisValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { formerAxisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1))); }
        List<AxisValue> latterAxisValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { latterAxisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 7))); }

        Axis formerAxisX = new Axis().setValues(formerAxisValues).setHasLines(true).setTextColor(Color.BLACK);
        Axis formerAxisY = new Axis().setHasLines(true).setTextColor(Color.BLACK);
        Axis latterAxisX = new Axis().setValues(latterAxisValues).setHasLines(true).setTextColor(Color.BLACK);
        Axis latterAxisY = new Axis().setHasLines(true).setTextColor(Color.BLACK);

        LineChartData formerLineChartData = new LineChartData();
        formerLineChartData.setLines(formerLines);
        formerLineChartData.setAxisXBottom(formerAxisX);
        formerLineChartData.setAxisYLeft(formerAxisY);

        LineChartData latterLineChartData = new LineChartData();
        latterLineChartData.setLines(latterLines);
        latterLineChartData.setAxisXBottom(latterAxisX);
        latterLineChartData.setAxisYLeft(latterAxisY);

        formerLineChartView.setLineChartData(formerLineChartData);
        formerLineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);

        latterLineChartView.setLineChartData(latterLineChartData);
        latterLineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = "本月收入："+ yearlyIncome.get(position) + "\n本月支出：" + yearlyExpense.get(position);
                textView.setText(text);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public void updateLineChart(){
        LineChartView formerLineChartView = null;
        LineChartView latterLineChartView = null;
        if(getView() != null){
            formerLineChartView = getView().findViewById(R.id.formerLineChartView);
            latterLineChartView = getView().findViewById(R.id.latterLineChartView);
        }

        List<PointValue> formerIncomeValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { formerIncomeValues.add(new PointValue(i, yearlyIncome.get(i))); }
        List<PointValue> formerExpenseValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { formerExpenseValues.add(new PointValue(i, yearlyExpense.get(i))); }
        List<PointValue> latterIncomeValues = new ArrayList<>(); for (int i = 6; i < 12; i++) { latterIncomeValues.add(new PointValue(i - 6, yearlyIncome.get(i))); }
        List<PointValue> latterExpenseValues = new ArrayList<>(); for (int i = 6; i < 12; i++) { latterExpenseValues.add(new PointValue(i - 6, yearlyExpense.get(i))); }

        Line formerIncomeLine = new Line(formerIncomeValues).setColor(Color.RED).setCubic(false); formerIncomeLine.setShape(ValueShape.CIRCLE); formerIncomeLine.setPointRadius(4);
        Line formerExpenseLine = new Line(formerExpenseValues).setColor(Color.BLACK).setCubic(false); formerExpenseLine.setShape(ValueShape.CIRCLE); formerExpenseLine.setPointRadius(4);
        Line latterIncomeLine = new Line(latterIncomeValues).setColor(Color.RED).setCubic(false); latterIncomeLine.setShape(ValueShape.CIRCLE); latterIncomeLine.setPointRadius(4);
        Line latterExpenseLine = new Line(latterExpenseValues).setColor(Color.BLACK).setCubic(false); latterExpenseLine.setShape(ValueShape.CIRCLE); latterExpenseLine.setPointRadius(4);

        List<Line> formerLines = new ArrayList<>(); formerLines.add(formerIncomeLine); formerLines.add(formerExpenseLine);
        List<Line> latterLines = new ArrayList<>(); latterLines.add(latterIncomeLine); latterLines.add(latterExpenseLine);

        List<AxisValue> formerAxisValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { formerAxisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1))); }
        List<AxisValue> latterAxisValues = new ArrayList<>(); for (int i = 0; i < 6; i++) { latterAxisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 7))); }

        Axis formerAxisX = new Axis().setValues(formerAxisValues).setHasLines(true).setTextColor(Color.BLACK);
        Axis formerAxisY = new Axis().setHasLines(true).setTextColor(Color.BLACK);
        Axis latterAxisX = new Axis().setValues(latterAxisValues).setHasLines(true).setTextColor(Color.BLACK);
        Axis latterAxisY = new Axis().setHasLines(true).setTextColor(Color.BLACK);

        LineChartData formerLineChartData = new LineChartData();
        formerLineChartData.setLines(formerLines);
        formerLineChartData.setAxisXBottom(formerAxisX);
        formerLineChartData.setAxisYLeft(formerAxisY);

        LineChartData latterLineChartData = new LineChartData();
        latterLineChartData.setLines(latterLines);
        latterLineChartData.setAxisXBottom(latterAxisX);
        latterLineChartData.setAxisYLeft(latterAxisY);

        if(formerLineChartView != null && latterLineChartView != null){
            formerLineChartView.setLineChartData(formerLineChartData);
            formerLineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);

            latterLineChartView.setLineChartData(latterLineChartData);
            latterLineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        }
    }
    public void onPause() {
        super.onPause();
    }
    public void onResume() {
        super.onResume();
        updateLineChart();
    }
    public void updateYearData(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(int i = 0;i < 12;i++) {
                    yearlyIncome.set(i,0);
                    yearlyExpense.set(i,0);
                }
            }
        };

        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        now.set(currentYear + 1, 0, 1, 0, 0, 0);
        Date nextYear = now.getTime();

        timer.schedule(task, nextYear);
    }
}
