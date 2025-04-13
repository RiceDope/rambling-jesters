package com.github.ricedope;

public class Main {
    
    public static void main(String[] args) {

        String response = Llama3Client.requester("Generate 100 names for a virtual agent called a Jester. Please only repond in the format 'name, name, name, ...' do not include any other text in the response");
        System.out.println(response);

    }

}
