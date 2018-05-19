package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import uk.ac.cam.groupseven.weatherapp.viewmodels.*;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.*;

import javax.swing.*;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<HomeViewModel>>>() {
        }).to(HomeViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<HourViewModel>>>() {
        }).to(HoursViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<CrestViewModel>>>() {
        }).to(CrestViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<DaysViewModel>>>() {
        }).to(DaysViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<ImageIcon>>() {
        }).to(UserCrestViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<MoreViewModel>>>() {
        }).to(MoreViewModelSource.class);
    }
}