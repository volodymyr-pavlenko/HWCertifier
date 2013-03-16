package se.zipper.hwcertifier.views;

import se.zipper.hwcertifier.domain.Computer;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 13.03.13
 * Time: 9:41
 * To change this template use File | Settings | File Templates.
 */
public interface HwCertifierView {
    public void renderComputer(Computer computer, String targetFolder);
}
