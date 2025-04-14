#!/bin/bash

mkdir "dirs"
cd dirs
# Create directories
for ((i=1; i<=10; i++)); do
    mkdir -p "Peer$i"
    num_files=$((1 + RANDOM % 10))  # Generate a random number of files (between 1 and 10)
    for ((j=1; j<=$num_files; j++)); do
        touch "Peer$i/file($i)_$j.txt"
        echo "This is a demo file in Peer$i" > "Peer$i/file($i)_$j.txt"
    done
done

echo "Directories and demo files created successfully."
