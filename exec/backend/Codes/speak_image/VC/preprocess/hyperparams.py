
class Hyperparams:
    # data
    # data = "/data/private/voice/LJSpeech-1.0"
    # data = "/data/private/voice/nick"
    # test_data = 'harvard_sentences.txt'
    max_duration = 10.0
    top_db = 15

    # signal processing
    sr = 22050  # Sample rate.
    n_fft = 2048  # fft points (samples)
    frame_shift = 0.0125  # seconds
    frame_length = 0.05  # seconds
    hop_length = int(sr * frame_shift)  # samples.
    win_length = int(sr * frame_length)  # samples.
    n_mels = 512  # Number of Mel banks to generate
    power = 1.2  # Exponent for amplifying the predicted magnitude
    n_iter = 100  # Number of inversion iterations
    preemphasis = .97  # or None
    max_db = 100
    ref_db = 20