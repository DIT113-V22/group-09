#! /bin/sh
virtualenv -p python3 $HOME/tmp/deepspeech-venv/
source $HOME/tmp/deepspeech-venv/bin/activate
read -p "Begin recording? (y/n)" yn
case $yn in
    y ) echo Recording, CTRL + C to end recording;
    arecord -r16000 speech.wav;
    deepspeech --model deepspeech-0.9.3-models.pbmm   --audio speech.wav| tee output.txt;;

    n ) echo exiting...;
    exit;;
    *) echo invalid response;
        exit 1;;
esac



