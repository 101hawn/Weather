package uk.ac.cam.groupseven.weatherapp.mocking;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherApiSource;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

public class WeatherApiSourceMock extends WeatherApiSource {
    @Inject
    private Weather weather;

    @Override
    public Observable<Weather> getWeatherNow() {
        return Observable.just(weather);
    }

    @Override
    public Observable<Weather> getWeatherInHours(int hours) {
        return Observable.just(weather);
    }
}
