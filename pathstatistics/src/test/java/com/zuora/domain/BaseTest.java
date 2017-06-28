package com.zuora.domain;

import com.zuora.application.command.SetupAccessPathsCommand;
import com.zuora.port.adapter.resources.PathStatisticsEventLog;
import org.junit.Before;

import java.util.Random;
import java.util.stream.Stream;

/**
 * @author by zy.
 */
public class BaseTest {
    public static final String[] PathStatisticsTestDataPaths = new String[]{
            "/","subscribers","filter","export"
            ,"catalog","edit","addUser","reg"
            ,"like","publish","fans","follow"
    };
    public static final String[] PathStatisticsTestDataUids = new String[]{
            "u1","u2","u3","u4","u5"
    };
    public static String[][] PathStatisticsTestData;

    static {
        Random random = new Random(System.currentTimeMillis());
        int genTestDataSize = random.nextInt(100);
        PathStatisticsTestData = new String[genTestDataSize * PathStatisticsTestDataUids.length][2];
        for (int out = 0; out < PathStatisticsTestDataUids.length; out++) {
            String uid = PathStatisticsTestDataUids[out];
            int base = out * genTestDataSize;
            for (int i = base; i < base+genTestDataSize; i++) {
                Random kvRandom = new Random(System.currentTimeMillis() + i);
                int pathsIdx = kvRandom.nextInt(PathStatisticsTestDataPaths.length - 1);
                //int uidsIdx = kvRandom.nextInt(PathStatisticsTestDataUids.length - 1);
                PathStatisticsTestData[i] = new String[]{
                        //PathStatisticsTestDataUids[uidsIdx],
                        uid,
                        PathStatisticsTestDataPaths[pathsIdx]
                };
            }
        }

        /*Stream.of(PathStatisticsTestData).forEach((arr)->{
            System.out.println(arr[0]+" "+arr[1]);
        });*/
        System.out.println("the PathStatisticsTestData has been inited");


        PathStatisticsTestData = new String[][]{
                new String[]{"u1","/"},
                new String[]{"u1","catalog"},
                new String[]{"u1","subscribers"},
                new String[]{"u2","catalog"},
                new String[]{"u2","subscribers"},

                new String[]{"u1","export"},
                new String[]{"u1","publish"},
                new String[]{"u1","subscribers"},
                new String[]{"u2","export"},
                new String[]{"u2","publish"},
                new String[]{"u3","subscribers"},

                new String[]{"u3","fans"},
                new String[]{"u3","edit"},
                new String[]{"u3","reg"},
                new String[]{"u3","export"},
                new String[]{"u3","publish"},
                new String[]{"u3","subscribers"},
                new String[]{"u3","fans"},
                new String[]{"u3","edit"},

                new String[]{"u4","fans"},
                new String[]{"u4","edit"},
                new String[]{"u4","reg"},
                new String[]{"u4","export"},
                new String[]{"u4","publish"},
                new String[]{"u4","subscribers"},
                new String[]{"u4","fans"},
                new String[]{"u4","edit"},

                new String[]{"u5","fans"},
                new String[]{"u5","edit"},
                new String[]{"u5","reg"},
                new String[]{"u6","fans"},
                new String[]{"u6","edit"},
                new String[]{"u6","reg"},
                new String[]{"u5","export"},
                new String[]{"u5","publish"},
                new String[]{"u5","subscribers"},
        };
    }

    protected SetupAccessPathsCommand setupAccessPathsCommand;

    @Before
    public void baseSetup(){
        PathStatisticsEventLog.registerSelf();
        setupAccessPathsCommand = new SetupAccessPathsCommand(PathStatisticsTestData);
    }
}
