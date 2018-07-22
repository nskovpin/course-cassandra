package ru.dataart.courses.cassandra.utils;

import java.util.Optional;
import java.util.function.Function;

public interface Status<Result> {

    boolean isSuccess();

    default <T, R> R execute(T input, Function<T, R> function){
        return function.apply(input);
    }

    Optional<Result> getResult();

}

class Success<R> implements Status<R>{

    private R result;

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Optional<R> getResult() {
        return Optional.empty();
    }


}

class Fail<R> implements Status{

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Optional getResult() {
        return Optional.empty();
    }
}