/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode trieNode = this;

        for (int i = 0; i < s.length(); i++) {
            String ch = Character.toString(s.charAt(i));


            if (!trieNode.children.containsKey(ch)) {
                trieNode.children.put(s.substring(0, i + 1), new TrieNode());
                trieNode = trieNode.children.get(s.substring(0, i + 1));
            } else {
                trieNode = trieNode.children.get(s.substring(0, i + 1));
            }

        }
        trieNode.isWord = true;

    }

    public boolean isWord(String s) {
        TrieNode iterator = this;
        for (int i = 0; i < s.length(); i++) {

            if (iterator.children.containsKey(s.substring(0, i + 1))) {
                iterator = iterator.children.get(s.substring(0, i + 1));
            } else {
                return false;
            }

        }
        return iterator.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode iterator = this;
        for (int i = 0; i < s.length(); i++) {
            if (iterator.children.containsKey(s.substring(0, i + 1))) {
                iterator = iterator.children.get(s.substring(0, i + 1));
                //System.out.println(s.substring(0,i + 1));
            } else {
                return "";
            }

        }

        return DFS(iterator, s, s);


    }

    public String getGoodWordStartingWith(String s) {
        List<String> aggregate = new ArrayList<String>();
        TrieNode iterator = this;
        for (int i = 0; i < s.length(); i++) {
            if (iterator.children.containsKey(s.substring(0, i + 1))) {
                iterator = iterator.children.get(s.substring(0, i + 1));
                //System.out.println(s.substring(0,i + 1));
            } else {
                return "";
            }

        }
        aggregate = DFSAggregate(iterator, s, s, aggregate);

        boolean notWordExists = false;

        for (String word : aggregate
        ) {
            if(!this.isWord(word)){
                notWordExists = true;
                break;
            }
        }
        Random random = new Random();
        if(notWordExists){
            List<String> notWords = new ArrayList<>();
            for (String word:aggregate
                 ) {
                if(!this.isWord(word)){
                    notWords.add(word);
                }
            }

            int num = random.nextInt(notWords.size());
            return notWords.get(num);
        } else{
            return aggregate.get(random.nextInt(aggregate.size()));
        }



    }

    public String DFS(TrieNode node, String prefix, String currentWord) {
        //System.out.println(node.isWord);
        if (currentWord.startsWith(prefix) && currentWord.compareTo(prefix) != 0 && node.isWord) {
            return currentWord;
        }
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            //System.out.println(currentWord + alphabet);
            if (node.children.containsKey(currentWord + alphabet)) {
                return DFS(node.children.get(currentWord + alphabet), prefix, currentWord + alphabet);
            }

        }
        return "";

    }

    public List<String> DFSAggregate(TrieNode node, String prefix, String currentWord, List<String> aggregate) {
        if (currentWord.startsWith(prefix) && currentWord.compareTo(prefix) != 0 && node.isWord) {
            aggregate.add(currentWord);
        }
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            //System.out.println(currentWord + alphabet);
            if (node.children.containsKey(currentWord + alphabet)) {
                return DFSAggregate(node.children.get(currentWord + alphabet), prefix, currentWord + alphabet, aggregate);
            }

        }
        return aggregate;


    }

    public static void main(String args[]) {
        TrieNode test = new TrieNode();
        test.add("bob");
        //System.out.println(test.isWord("bob"));
        System.out.println(test.getGoodWordStartingWith("bo"));

    }

}
