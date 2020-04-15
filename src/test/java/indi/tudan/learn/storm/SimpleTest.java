package indi.tudan.learn.storm;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SimpleTest {

    public static void main(String[] args) {

        String zkServers = "10.19.83.82,10.19.83.84,10.19.83.85,10.19.83.86,10.19.83.87";
        String brokerInfo = Arrays.stream(zkServers.split(",", -1))
                .map(item -> String.format("%s:%s", item, 2181)).collect(Collectors.toSet()).toString();
        System.out.println(brokerInfo);

        brokerInfo = brokerInfo.substring(1, brokerInfo.length() - 1);

        System.out.println(brokerInfo);

    }

}
