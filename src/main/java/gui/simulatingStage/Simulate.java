package gui.simulatingStage;

import units.cache.Block;
import units.cache.Cache;
import static units.instructionUnit.dispatch;
import static units.instructionUnit.execute;
import static units.instructionUnit.instructionTable;
import static units.instructionUnit.lastInstructionIndex;
import static units.instructionUnit.writeBack;
import units.stage.aluStage.CacheStage;
import static units.stage.aluStage.CacheStage.cacheTable;

public class Simulate {
	public static int cycle = 0;

	public static void Simulate() throws Exception {
//		adderTable.get(0).setBusy(true);
//		loadTable.get(0).setAddress("505");
//		System.out.println(instructionTable.get(0).toString());
		// w

		// If no stations are busy and lastInstructionIndex == instructionTable.size()
		// Pop-up execution complete (calls a function)

		execute();
		writeBack();
		if (lastInstructionIndex != instructionTable.size()) {
			dispatch();
		}
				for (int i = 0; i < Cache.blockCount; i++) {
			String blockName = "Block " + i;
			Block current = Cache.getBlockWithName(blockName);
			for (int j = 0; j < Cache.blockSize; j++) {
				String binaryString = String.format("%8s", Integer.toBinaryString(current.getByte(j))).replace(' ', '0');
				cacheTable.add(new CacheStage(current.getBlockName(), binaryString, current.getTag()));
				cacheTable.get(0).setAddress(55);
			}
		}
		cycle++;
	}
}
