class SaisStep:
        def __init__(self, S = [], t = [], lmsPointersArray = [],
                     bucketHash = dict(), keySet = [], bucketPointers = dict(),
                     arrayWithNewNames = [], SA = [], lmsPointersHash = dict()):
                self.S = S
                self.t = t
                self.lmsPointersArray = lmsPointersArray
                self.bucketHash = bucketHash
                self.keySet = keySet
                self.bucketPointers = bucketPointers
                self.arrayWithNewNames = arrayWithNewNames
                self.SA = SA
                self.lmsPointersHash = lmsPointersHash
                

        def getArrayWithNewNames(self):
                return self.arrayWithNewNames
        

        def setArrayWithNewNames(self, arrayWithNewNames):
                self.arrayWithNewNames = arrayWithNewNames
        

        def  getS(self):
                return self.S
        

        def  setS(self, s):
                self.S = s
        

        def  getT(self):
                return self.t
        

        def  setT(self, t):
                self.t = t
        

        def  getLmsPointersArray(self):
                return self.lmsPointersArray
        

        def  setLmsPointersArray(self, lmsPointersArray):
                self.lmsPointersArray = lmsPointersArray
        

        def  getBucketHash(self):
                return self.bucketHash

        def  setBucketHash(self, bucketHash):
                self.bucketHash = bucketHash
        

        def getKeySet(self):
                return self.keySet
        

        def setKeySet(self, keySet):
                self.keySet = keySet
        

        def getBucketPointers(self):
                return self.bucketPointers
        

        def  setBucketPointers(self, bucketPointers):
                self.bucketPointers = bucketPointers
        
        def getArrayWithNewNamesForEachLMSSubstring(self, bucketHash, S, t):
                print ("Getting new names...")
        
                S1 = []
                SA = []

                for i in range(0, len(self.lmsPointersHash)):
                        S1.append("")
                
                for key in sorted(bucketHash.keys()):
                        for InBucket in bucketHash[key]:
                                SA.append(InBucket)
                        
                newName = 0
                lastKnownIndexOfLMSSubInSA = 0
                for i in range(0, len(SA)):
                        if str(SA[i]) in self.lmsPointersHash.keys():
                                lastKnownIndexOfLMSSubInSA = i
                                break
                        
                S1[self.lmsPointersHash[str(SA[lastKnownIndexOfLMSSubInSA])]] = str(newName)
                
                for i in range(lastKnownIndexOfLMSSubInSA + 1, len(SA)):
                        if str(SA[i]) in self.lmsPointersHash:
                                if self.compareLMSSubstrings(SA[i], SA[lastKnownIndexOfLMSSubInSA], S, t) == True:
                                        S1[self.lmsPointersHash[str(SA[i])]] = str(newName)
                                else:
                                        newName += 1
                                        S1[self.lmsPointersHash[str(SA[i])]] = str(newName)
                                lastKnownIndexOfLMSSubInSA = i
                         
        
                print ("Done finding new names")
                self.SA = SA
                return S1

        def doThirdIteration(self, S, t,
                        bucketHash,
                        bucketPointers, keySet):
                
                # Iterate from the end to the beginning
                for ii in range(0, len(keySet)):
                        i = len(keySet) - 1 - ii
                   
                        for jj in range(0, len(bucketHash[str(keySet[i])])):
                                j = len(bucketHash[str(keySet[i])]) - 1 - jj
                                InBucket = bucketHash[str(keySet[i])][j]
                                if InBucket > 0 and t[InBucket - 1] == False:
                                        bucketHash[str(S[InBucket - 1])][bucketPointers[str(S[InBucket - 1])]] = InBucket - 1
                                        bucketPointers[str(S[InBucket - 1])] = bucketPointers[str(S[InBucket - 1])] - 1
                
        def doSecondIteration(self, S, t, bucketHash, bucketPointers):
                # Iterate over all buckets
                
                for key in sorted(bucketHash.keys()):
                        for InBucket in bucketHash[key]:
                                if InBucket > 0 and t[InBucket - 1] == True:
                                        bucketHash[str(S[InBucket - 1])][bucketPointers[str(S[InBucket - 1])]] = InBucket - 1
                                        bucketPointers[str(S[InBucket - 1])] = bucketPointers[str(S[InBucket - 1])] + 1
               
        #
        # Create SA buckets and insert -1 for each value
        #
        def getBucketHashFromSAndInitializeToMinusOne(self, S):

                bucketHash = dict()
                for i in range(0, len(S)):
                        
                        if str(S[i]) in bucketHash.keys():
                                bucketHash[str(S[i])].append(-1)
                        else:
                                values = []
                                values.append(-1)
                                bucketHash[str(S[i])] = values
        
                return bucketHash

        def setSAToMinusOne(self, bucketHash):
                for key in sorted(bucketHash.keys()):
                        for i in range(0, len(bucketHash[key])):
                                bucketHash[key][i] = -1
                   
            
        def  joinArray(self, aArr):
            return ''.join(str(x) for x in aArr)
        
        
        def  compareLMSSubstrings(self, firstLMS, secondLMS, S, t):
                isFirstIteration = True
                while S[firstLMS] == S[secondLMS]:

                        # Do only once
                        if isFirstIteration == True:
                                if t[firstLMS] == False and t[secondLMS] == False:
                                        firstLMS += 1
                                        secondLMS += 1
                                        # Check if we're comparing last lms s
                                        if firstLMS == secondLMS and firstLMS >= len(S):
                                                return True
                                        isFirstIteration = False
                                        continue
                                
                        
                        
                        if t[firstLMS] == False and t[secondLMS] == False:
                                return True
                        elif t[firstLMS] == True and t[secondLMS] == True:
                                firstLMS += 1
                                secondLMS += 1
                        else:
                                return False
                        
                
                return False
        
        
        def setAllPointersInBucketsToBeginning(self, keySet, bucketPointers):
               for i in range(0, len(keySet)):
                        bucketPointers[str(keySet[i])] = 0
                
        
        
        def setAllPointersInBucketsToEnd(self, keySet, bucketPointers, bucketHash):  
                for i in range(0, len(keySet)):
                        bucketPointers[str(keySet[i])] = len(bucketHash[str(keySet[i])]) - 1
              
        
        
        #      Returns array of s that tells us the types of characters
        #      L = True
        #      S = False
        # 
        def getTFromS(self, S):
               
                t = []
                for i in range(0, len(S)):
                        t.append(False)
                t[len(S) - 1] = False
                t[len(S) - 2] = True

                for ii in range(1, len(S) - 2):
                        i = len(S) - 2 - ii
                        if S[i] < S[i + 1]:
                                t[i] = False
                        elif S[i] > S[i + 1]:
                                t[i] = True
                        else:
                                t[i] = t[i + 1]
                        
                
                return t
        

        #
        #      Set array from "i" to "j" to Value
        # 
        #
        def setArray(self, t, i, j,  Value):
                while i <= j:
                        t[i] = Value
                        i += 1
             
        #
        # Returns array holding pointers to LMS-subs
        # S is the input
        # [] t keeps track of S-types and L-types
        # S type = False
        # L type = True
        # 
        def  getLMSPointersHash(self, S, t):
                lmsPointersHash = dict()
                lmsPointers = []
                for i in range(1, len(S)):
                        if t[i - 1] == True and t[i] == False:
                                lmsPointers.append(i)
                                lmsPointersHash[str(i)] = len(lmsPointers) - 1
                                
                self.lmsPointersHash = lmsPointersHash
                self.lmsPointersArray = lmsPointers
                return lmsPointers
        

        def  getLmsPointersHash(self):
                return self.lmsPointersHash
        

        def setLmsPointersHash(self, lmsPointersHash):
                self.lmsPointersHash = lmsPointersHash
        

        def  printArray(self, arr,  message):
                print (message)
                print (arr)
        
