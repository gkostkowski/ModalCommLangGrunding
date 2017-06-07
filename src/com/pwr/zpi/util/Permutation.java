package com.pwr.zpi.util;

import com.pwr.zpi.language.Formula;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * Class enables permute certain List.
 *
 * Based on code at http://www.programcreek.com.
 */
public class Permutation {

    static public <Formula>void  nextPermutation(ArrayList<Formula> nums, Comparator<Formula> c) {

        if(nums == null || nums.size()<2)
            return;

        int p=0;
        for(int i=nums.size()-2; i>=0; i--){
            if(c.compare(nums.get(i), nums.get(i+1))==-1){
                p=i;
                break;
            }
        }

        int q = 0;
        for(int i=nums.size()-1; i>p; i--){
            if(c.compare(nums.get(i), nums.get(p)) == 1){
                q=i;
                break;
            }
        }

        if(p==0 && q==0){
            reverse(nums, 0, nums.size()-1);
            return;
        }

        reverse(nums, p, q);

        if(p<nums.size()-1){
            reverse(nums, p+1, nums.size()-1);
        }
    }

    static private <Formula>void reverse(ArrayList<Formula> nums, int left, int right){
        Collections.swap(nums, left, right);
    }

    public static List<ArrayList<Formula>> getAllPossiblePermutations(ArrayList<Formula> elems, Comparator<Formula> comp) {
        Set<ArrayList<Formula>> t = new HashSet<>();
        for (int i = 0; i < factorial(elems.size()); i++) {
            t.add(new ArrayList<Formula>(elems));
            Permutation.nextPermutation(elems, comp);
        }

        List<ArrayList<Formula>> res = new ArrayList<>();
        for (int i = t.iterator().next().size(); i > 1; i--)
            res.addAll(t = t
                    .stream()
                    .map(l -> new ArrayList<>(l.subList(0, l.size() - 1)))
                    .collect(toSet()));

        return res;
    }

    private static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++)
            fact *= i;
        return fact;
    }
}
