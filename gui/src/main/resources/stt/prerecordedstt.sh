#! /bin/sh
virtualenv -p python3 $HOME/tmp/deepspeech-venv/
source $HOME/tmp/deepspeech-venv/bin/activate
echo Please provide the path to the audio file you wish to transcribe.
read varpath

    deepspeech --model deepspeech-0.9.3-models.pbmm   --audio $varpath | tee output.txt


