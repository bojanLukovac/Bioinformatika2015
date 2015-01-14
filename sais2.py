import string


class CouldntFindSentinel(Exception):
    pass


class WrongBucket(Exception):
    pass


def compare_lex_size(array, smaller):
    return all([ord(member)>ord(smaller) for member in array])


class SuffixArray(object):
    def __init__(self, input_array):
        self.array = input_array
        self.s = self._find_sentinel()
        self._array = [character for character in self.array] + [self.s]
        #self.t = type_array()

    def _find_sentinel(self):
        for sentinel in string.punctuation:
            if not sentinel in self.array and compare_lex_size(self.array, sentinel):
                return sentinel
        raise CouldntFindSentinel("No sentinel character possible")


class Bucket(object):
    def __init__(self):
        self._members = []

    def __add__(self, new_member):
        for member in self._members:
            if not member.startswith(new_member[0]):
                raise WrongBucket("Wrong bucket member")
        self._members.append(new_member)

    def __len__(self):
        return len(self._members)

    def join(self):
        return "".join(self._members)


class BucketList(object):
    def __init__(self):
        self.buckets = {}

    def add_suffix(self, suffix):
        if suffix[0] in self.buckets:
            self.buckets[suffix[0]] + suffix
        else:
            self.buckets[suffix[0]] = Bucket()
            self.buckets[suffix[0]] + suffix

    def __getitem__(self,a):
        return self.buckets[a]._members

    def expose_buckets(self):
        return [v for k,v in self.buckets]


class SAIS(object):
    def __init__(self, string):
        self.original_s = string

    def typify(self, in_stri, n, end):


        types = [-1 for i in range(n)]

        types[:-1], types[:-2] = 1, 0

        counter = {}

        counter[in_stri[:-1]] += 1
        counter[in_stri[:-2]] += 1

        for i in range(len(in_stri[:-2]))[::-1]:
            types[i] = 1 if (
                    in_stri[i] < in_stri[i + 1] or 
                    in_stri[i] == in_stri[i + 1] and
                    types[i+1] == 1 ) else 0

            if in_stri[i] in counter.keys():
                counter[in_stri[i]] += 1
            else:
                counter[in_stri[i]] = 1

        total = 0

        bucket = {}
        for i,v in counter.iteritems():
            total += v
            bucket[i] = total - 1

        return types, bucket




    def run(self, in_stri):
        n = len(in_stri)



