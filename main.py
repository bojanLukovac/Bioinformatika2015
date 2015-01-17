import sys
import time

from saisStep import SaisStep

def areAllElementsDifferent(x):
    return len({}.fromkeys(x)) == len(x)

def doSAIS(S):
    saisStep = SaisStep()
    saisStep.setS(S)
    print ("Got S split to array.")
    print ("S.length = " + str(len(S)))

    t = saisStep.getTFromS(S)
    saisStep.setT(t)
    print ("Got L and S types")

    lmsPointersArray = saisStep.getLMSPointersHash(S, t)
    saisStep.setLmsPointersArray(lmsPointersArray)

    print ("Got lmsPointersArray")

    bucketHash = saisStep.getBucketHashFromSAndInitializeToMinusOne(S)
    saisStep.setBucketHash(bucketHash)

    print ("Got bucketHash")
   
    keySet = sorted(bucketHash.keys())
    saisStep.setKeySet(keySet)
    
    print ("Got keyset" )
    #
    #	HashMap that stores pointers on bucket arrays
    # 	For example, this is how it's done in powerpoint presentations: 
    # 	B['$'] = 0 B['A'] = 3 B['C'] = 5 B['G'] = 9 B['T'] = 11
    #	We do the similar thing here :)
    # 
    bucketPointers = dict()
   
    saisStep.setBucketPointers(bucketPointers)
    
    saisStep.setAllPointersInBucketsToEnd(keySet, bucketPointers, bucketHash)

    
    #
    # Algorithm 1, first iteration
    #
    for lmsPointer in lmsPointersArray:
        lms = S[lmsPointer]
        bucketHash[str(lms)][bucketPointers[str(lms)]] = lmsPointer
        bucketPointers[str(lms)] = bucketPointers[str(lms)] - 1
    
    print ("Done with first step")
    
    #
    # Values after first iteration
    #
    
    #
    # Algorithm 1, second iteration
    # Set all pointers in buckets to beginning
    #
    saisStep.setAllPointersInBucketsToBeginning(keySet, bucketPointers)
    saisStep.doSecondIteration(S, t, bucketHash, bucketPointers)
    print ("Done with second step")
    
    #
    # Algorithm 1, third iteration
    # Set all pointers in buckets to end
    #
    saisStep.setAllPointersInBucketsToEnd(keySet, bucketPointers, bucketHash)
    saisStep.doThirdIteration(S, t, bucketHash, bucketPointers, keySet)
    print ("Done with third step")
   
    # STEP 4,  Get new array with new name for each LMS substring
   
    S1 = saisStep.getArrayWithNewNamesForEachLMSSubstring(bucketHash, S, t)
    saisStep.setArrayWithNewNames(S1)
    
    return saisStep

saisSteps = []
fin = open(sys.argv[1], "r")
inputStr = list(''.join(fin.readlines()) + "$")
inputStr = [x for x in inputStr if x >= 'A' and x <= 'Z' or x == '$']
fin.close()
copiedOriginal = inputStr



while True:
    print("Entered SA-IS iteration")
    print ("_________________________")
    solution = doSAIS(inputStr)
    saisSteps.append(solution)
    if areAllElementsDifferent(solution.getArrayWithNewNames()) == False:
        inputStr = solution.getArrayWithNewNames()
    else:
        break

methods = saisSteps[0]

# GET last SA from last S
lastS = saisSteps[len(saisSteps) - 1].getArrayWithNewNames()
lastSA = []
bucketHash = dict()
bucketPointers = dict()
lmsPointers = []

for i in range(0, len(lastS)):
    lastSA.append("0")


for i in range(0, len(lastS)):
    lastSA[int(lastS[i])] = str(i)


for ii in range(0, len(saisSteps)):
    i = len(saisSteps) - 1 - ii
    
    step = saisSteps[i]
    bucketHash = step.getBucketHash()
    bucketPointers = step.getBucketPointers()
    # Do the first step in recovery

    step.setSAToMinusOne(bucketHash)

    step.setAllPointersInBucketsToEnd(step.getKeySet(), bucketPointers, bucketHash)

    lmsPointers = step.getLmsPointersArray()

    if i == 0 :
        lastS = copiedOriginal
    else:
        lastS = saisSteps[i - 1].getArrayWithNewNames()

    
    for jj in range(0, len(lastSA)):
        j = len(lastSA) - 1 - jj
        
        index = lastSA[j]
        
        lmsPointer = lmsPointers[int(index)]
        
        correspondingSuffix = lastS[lmsPointer]
       
        
        bucketHash[str(correspondingSuffix)][bucketPointers[str(correspondingSuffix)]] = lmsPointer
        
        bucketPointers[str(correspondingSuffix)] = bucketPointers[str(correspondingSuffix)] - 1
        

    step.setAllPointersInBucketsToBeginning(step.getKeySet(), bucketPointers)
    methods.doSecondIteration(step.getS(), step.getT(), bucketHash, bucketPointers)

    step.setAllPointersInBucketsToEnd(step.getKeySet(), bucketPointers, bucketHash)
    methods.doThirdIteration(step.getS(), step.getT(), bucketHash, bucketPointers, step.getKeySet())


    lastSA = []
    for key in sorted(bucketHash.keys()):
        for b in range(0, len(bucketHash[key])):
            lastSA.append(str(bucketHash[key][b]))

    
    if (i == 0):
        
        # We're done, show times and memory usage and write solution to file
        
        mb = 1024 * 1024


        print ("lastSA is printed to file: " + sys.argv[2])
        out = open(sys.argv[2], "w")
        for j in range(0, len(lastSA)):
            #out.write(str(''.join(inputStr[int(lastSA[j]) :])) + " " + str(lastSA[j]) + "\n")
            out.write(str(lastSA[j]) + " ")
        out.close()
    
