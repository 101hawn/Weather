package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BigTextStyle;
import uk.ac.cam.groupseven.weatherapp.styles.HoursTableStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourlyWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class HoursScreen implements Screen {
    @Inject
    ViewModelSource<Loadable<HourViewModel>> viewModelSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(BackgroundStyle.class)
    private JButton leftButton;
    @ApplyStyle(BackgroundStyle.class)
    private JButton rightButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;
    @ApplyStyle({BackgroundStyle.class, HoursTableStyle.class})
    private JTable hoursTable;
    @ApplyStyle(BackgroundStyle.class)
    private JScrollPane scrollPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle({BackgroundStyle.class, BigTextStyle.class})
    private JLabel timeLabel;

    private JList<Object> list;

    @Override
    public Disposable start() {
        return viewModelSource.getViewModel(getRefreshObservable()).subscribe(this::updateScreen);
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));
    }

    private void updateScreen(Loadable<HourViewModel> viewModelLoadable) {
        if (viewModelLoadable.getLoading()) {
            timeLabel.setText("Loading...");
        } else if (viewModelLoadable.getError() != null) {
            timeLabel.setText("Error");
        } else {


            HourViewModel viewModel = viewModelLoadable.getViewModel();
            assert viewModel != null;


            timeLabel.setText(viewModel.getCurrentTime());


            hoursTable.setModel(new DefaultTableModel() {


                @Override
                public int getRowCount() {
                    return viewModel.getHourlyWeather().size();
                }

                @Override
                public int getColumnCount() {
                    return 3;
                }


                @Override
                public Object getValueAt(int row, int column) {
                    HourlyWeather hourlyWeather = viewModel.getHourlyWeather().get(row);
                    switch (column) {
                        case 0:
                            return hourlyWeather.getTimeText();
                        case 1:
                            return hourlyWeather.getTemperatureText();
                        case 2:
                            return hourlyWeather.getWindText();
                        default:
                            return null;
                    }
                }


            });


            hoursTable.getColumnModel().getColumn(0)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JLabel jLabel = new JLabel((String) value);
                        jLabel.setFont(table.getTableHeader().getFont());
                        jLabel.setForeground(table.getTableHeader().getForeground());
                        return jLabel;
                    });
            hoursTable.getColumnModel().getColumn(1)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> new JLabel((Icon) value));
            hoursTable.getColumnModel().getColumn(2)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> new JLabel((Icon) value));
            Image scaledTempIcon = new ImageIcon("./res/icons/thermometer.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            Image scaledWindIcon = new ImageIcon("./res/icons/wind.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);

            hoursTable.getColumnModel().getColumn(0).setHeaderValue("Time");
            hoursTable.getColumnModel().getColumn(1).setHeaderValue(new ImageIcon(scaledTempIcon));
            hoursTable.getColumnModel().getColumn(2).setHeaderValue(new ImageIcon(scaledWindIcon));

            hoursTable.invalidate();
        }

    }

    public JPanel getPanel() {
        return panel;
    }


    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }
}
