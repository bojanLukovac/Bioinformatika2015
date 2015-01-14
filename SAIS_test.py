import unittest
import string
import random
import sais

class TestSuffixArray(unittest.TestCase):

    def setUp(self):
        self.testConst = sais.SuffixArray(string.letters)

    #constructor tests
    def test_invalid(self):
        self.assertRaises(sais.CouldntFindSentinel, sais.SuffixArray, string.punctuation)

    def test_invalid_smallest(self):
        self.assertRaises(sais.CouldntFindSentinel, sais.SuffixArray, "\x00")

    def test_valid(self):
        self.assertTrue(all([i in self.testConst._array for i in string.letters + self.testConst.s]))

    def test_sentinel_position(self):
        self.assertTrue(self.testConst._array[-1] == self.testConst.s)

class TestBucket(unittest.TestCase):

    def testAddValid(self):
        self.testCase = sais.Bucket()
        self.testCase + "ab"
        self.assertEqual(len(self.testCase), 1)

    def testAddValidMultiple(self):
        self.testCase = sais.Bucket()
        self.testCase + "aba"
        self.testCase + "abac"
        self.assertEqual(len(self.testCase), 2)

    def testAddInvalidMember(self):
        self.testCase = sais.Bucket()
        self.testCase + "ab"
        self.assertRaises(sais.WrongBucket, self.testCase.__add__, "ba")

class TestBucketList(unittest.TestCase):

    def testAddNewBucket(self):
        self.testCase = sais.BucketList()
        self.testCase.add_suffix("asd")
        self.assertEqual(self.testCase["a"], self.testCase["a"]._members)


if __name__ == "__main__":
    unittest.main()
