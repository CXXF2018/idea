package com.atjava;


    enum Season1{
        SPRING("cuntian","cdss"),
        SUMMER("xiatian","cscs"),
        AUTUMN("qiutian","asdas"),
        WINTER("dongtian","scs");

        private final String SeasonName;
        private final String SeasonDesc;

        Season1(String seasonName, String seasonDesc) {
            SeasonName = seasonName;
            SeasonDesc = seasonDesc;
        }

        public String getSeasonName() {
            return SeasonName;
        }

        public String getSeasonDesc() {
            return SeasonDesc;
        }
    }

    public class EnumTest {
        public static void main(String[] args) {
            System.out.println(Season1.SPRING);
            //默认打印spring的名字

            Season1[] values = Season1.values();
            for (int i = 0; i <values.length ; i++) {
                System.out.println(values[i]);
            }

            Season1 season1 = Season1.valueOf("SPRING");
            System.out.println(season1);

        }
    }
