package com.example.thread.lambda;

import com.example.thread.test.Test1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName
 * @Description
 *
 * @Autor wcy
 * @Date 2020/12/7 13:34
 */
public class LambdaTest {
    public static void main(String[] args) {
        test1();
    }


    /**
     *
     */
    public static void test1(){
        List<Integer> nums = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            nums.add(i);
        }
        List<Integer> collect = nums.stream().filter(num -> num % 2 == 0).collect(Collectors.toList());
        collect.forEach(x -> {
            System.out.println(x);
        });

        // 初始化
        List<Student> students = new ArrayList<Student>() {
            {
                add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
                add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
                add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
                add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
                add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
                add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
                add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
                add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
                add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
                add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
            }
        };

        //过滤武汉大学得数据 filter
        List<Student> stu1 = students.stream().filter(student -> "武汉大学".equals(student.getSchool())).collect(Collectors.toList());
        stu1.stream().forEach(x->{
            System.out.println("stu1-->"+x.getName());
        });
        //limit   返回前n条数据
        List<Student> stu2 = students.stream().filter(student -> "土木工程".equals(student.getMajor())).limit(2).collect(Collectors.toList());
        stu2.stream().forEach(x->{
            System.out.println("stu2-->"+x.getName());
        });
        //排序后，limit 返回数据
        List<Student> stu3 = students.stream().filter(student -> "土木工程".equals(student.getMajor())).sorted((s1, s2) -> s1.getAge() - s2.getAge())
            .limit(5).collect(Collectors.toList());
        stu3.stream().forEach(x->{
            System.out.println("stu3-->"+x.getName());
        });
        //映射处理  查询所有学生姓名
        List<String> stuName = students.stream().map(x->x.getName()).collect(Collectors.toList());
        stuName.forEach(x-> System.out.println("学生姓名"+x));
        // 查询学科为计算机科学得学生姓名，将学生姓名映射为字符串
        List<String> stu4 = students.stream().filter(student -> "计算机科学".equals(student.getMajor())).map(Student::getName).collect(Collectors.toList());
        stu4.forEach(x-> System.out.println("计算机科学学生姓名--》"+x));

        //归约    统计所有专业为计算机科学学生的年龄之和
        int sum = students.stream().filter(student -> "计算机科学".equals(student.getMajor())).mapToInt(Student::getAge).sum();
        System.out.println("*********************"+sum);

        Integer reduce = students.stream().filter(student -> "计算机科学".equals(student.getMajor())).map(Student::getAge).reduce(0, (a, b) -> a + b);
        System.out.println("*********************"+reduce);

        Integer reduce1 = students.stream().filter(student -> "计算机科学".equals(student.getMajor())).map(Student::getAge).reduce(0, Integer::sum);
        System.out.println("*********************"+reduce1);

        Optional<Integer> optional = students.stream().filter(student -> "计算机科学".equals(student.getMajor())).map(Student::getAge).reduce(Integer::sum);
        System.out.println("*********************"+optional.get());

        //收集
        long count = students.stream().count();
        System.out.println("总人数"+count);

       // int sum1 = students.stream().mapToInt(Student::getAge).sum();
        int totalAge = students.stream().collect(Collectors.summingInt(Student::getAge));
        System.out.println("总年龄"+totalAge);

        //求年龄得最大值、最小值
        Optional<Student> maxAge1 = students.stream().collect(Collectors.maxBy((s1, s2) -> s1.getAge() - s2.getAge()));
        Optional<Student> maxAge2 = students.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getAge)));
        Optional<Student> minAge = students.stream().collect(Collectors.minBy(Comparator.comparing(Student::getAge)));

        System.out.println(maxAge1.get().getAge()+"---"+maxAge2.get().getAge()+"---"+minAge.get().getAge());
    }

}