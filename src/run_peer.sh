#!/bin/bash

# Usage: ./run_peer.sh <number_of_super_peers> <topology_type>

peer_number=$1
topology_type=$2
jar_path="../target/pa3-1.0-SNAPSHOT.jar"
#base_dir="../dirs/Peer"
base_dir=$(grep "^base_dir=" config.properties | cut -d'=' -f2)
# Set topology path based on topology type
if [ "$topology_type" == "T" ]; then
    topology_path="tree-topology.txt"
elif [ "$topology_type" == "A2A" ]; then
    topology_path="all-to-all-topology.txt"
else
    echo "Invalid topology type. Please specify 'T' for tree topology or 'A2A' for all-to-all topology."
    exit 1
fi

java -jar "$jar_path" "$topology_path" "$peer_number" "${base_dir}${peer_number}"
