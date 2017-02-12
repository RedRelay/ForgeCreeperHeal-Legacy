package _new_old;

public interface ISerializer<R, W, T> {
	void read(R reader, T to);
	void write(W writer, T from);
}
