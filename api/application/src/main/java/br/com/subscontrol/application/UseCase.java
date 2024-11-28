package br.com.subscontrol.domain.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);

}
