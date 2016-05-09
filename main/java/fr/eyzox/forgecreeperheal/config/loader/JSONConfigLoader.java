package fr.eyzox.forgecreeperheal.config.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import fr.eyzox.forgecreeperheal.config.Config;
import fr.eyzox.forgecreeperheal.config.ConfigOptionGroup;
import fr.eyzox.forgecreeperheal.config.option.ConfigOption;
import fr.eyzox.forgecreeperheal.config.option.ConfigOptionList;
import fr.eyzox.forgecreeperheal.config.option.IConfigOption;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;
import fr.eyzox.forgecreeperheal.exception.config.InvalidConfigValueException;
import fr.eyzox.forgecreeperheal.exception.config.InvalidDataTypeException;

public class JSONConfigLoader extends AbstractFileConfigLoader {

	public JSONConfigLoader(File file) {
		super(file);
	}

	@Override
	public void load(Config config) throws NoSuchFileException, FileNotFoundException, AccessDeniedException, IOException, InvalidConfigValueException {
		super.load(config);
		InputStream input = null;
		try {
			input = new FileInputStream(getFile());
			this.readJsonStream(getGson(), input, config);
		}finally {
			if(input != null) {
				input.close();
			}
		}

	}

	@Override
	public void save(Config config) throws FileNotFoundException, AccessDeniedException, IOException {
		super.save(config);
		OutputStream output = null;
		try {
			output = new FileOutputStream(getFile());
			this.writeJsonStream(getGson(), output, config);
		}finally {
			if(output != null) {
				output.close();
			}
		}
	}

	private void readJsonStream(final Gson gson, final InputStream in, final Config config) throws IOException, InvalidConfigValueException {
		final JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

		reader.beginObject();
		while (reader.hasNext()) {

			final String groupOptionName = reader.nextName();
			final ConfigOptionGroup optionGroup = config.getOptionGroup(groupOptionName);

			if(optionGroup != null) {

				if(reader.peek() != JsonToken.BEGIN_OBJECT) {
					getErrorManager().error(new ForgeCreeperHealException(optionGroup.getName() + " : " + new InvalidDataTypeException(JsonToken.BEGIN_OBJECT.name(), reader.peek().name()).getMessage()));
					reader.skipValue();
				}

				//Move to groupOption {...}
				reader.beginObject();

				while(reader.hasNext()) {
					final String optionName = reader.nextName();
					final IConfigOption option = optionGroup.getOption(optionName);

					if(option != null) {
						try {
							if(option instanceof ConfigOption) {
								readConfigOption(reader, (ConfigOption)option);
							}else if(option instanceof ConfigOptionList) {
								readConfigOptionList(reader, (ConfigOptionList)option);
							}else {
								//ERROR : Unknow type (this should never happens)
								throw new RuntimeException("Unknown config option type !");
							}
						}catch(ForgeCreeperHealException e) {
							getErrorManager().error(new ForgeCreeperHealException(option.getName()+" : "+e.getMessage()));
						}
					}else {
						//warn : Unhandled Option (User has added an option in his configFile that doesn't exist)
						getErrorManager().error(new ForgeCreeperHealException(optionName+" is not a valid option name"));
						reader.skipValue();
					}
				}
				//Exit groupOption{...}
				reader.endObject();
			}else {
				//Unhandled OptionGroup (User has added an optionGroup in his configFile that doesn't exist)
				getErrorManager().error(new ForgeCreeperHealException(groupOptionName+" is not a valid option group name"));
				reader.skipValue();
			}
		}

		reader.endObject();
		reader.close();
	}

	private String getOptionValue(final JsonReader reader, final IConfigOption option) throws IOException {
		final JsonToken token = reader.peek();
		switch (token) {
		case BOOLEAN:
			return Boolean.toString(reader.nextBoolean());
		case NUMBER:
			return Integer.toString(reader.nextInt());
		case STRING:
			return reader.nextString();
		default:
			reader.skipValue();
			throw new InvalidDataTypeException("BOOLEAN or NUMBER or STRING", token.name());
		}
	}

	private void readConfigOption(final JsonReader reader, final ConfigOption configOption) throws IOException, ForgeCreeperHealException {
		String value = getOptionValue(reader, configOption);
		configOption.setValue(value);
	}

	private void readConfigOptionList(final JsonReader reader, final ConfigOptionList configOptionList) throws IOException, InvalidConfigValueException {

		final JsonToken token = reader.peek();

		if(token != JsonToken.BEGIN_ARRAY) {
			reader.skipValue();
			throw new InvalidDataTypeException("ARRAY", token.name());
		}

		reader.beginArray();

		final List<String> values = new LinkedList<String>();
		while(reader.hasNext()) {
			String value = null;
			try {
				value = getOptionValue(reader, configOptionList);
				values.add(value);
			}catch(ForgeCreeperHealException e) {
				getErrorManager().error(new ForgeCreeperHealException(configOptionList.getName()+" : "+e.getMessage()));
			}

		}

		reader.endArray();

		configOptionList.setValues(values);

	}

	private void writeJsonStream(final Gson gson, final OutputStream out, final Config config) throws IOException {
		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
		writer.setIndent("  ");
		writer.beginObject();
		for(final ConfigOptionGroup group : config.getOptionGroups()){
			writer.name(group.getName());
			writer.beginObject();
			for(final IConfigOption option : group.getOptions()) {
				if(option instanceof ConfigOption) {
					writeConfigOption(writer, (ConfigOption) option);
				}else if(option instanceof ConfigOptionList) {
					writeConfigOptionList(writer, (ConfigOptionList) option);
				}
			}
			writer.endObject();
		}
		writer.endObject();
		writer.close();
	}

	private void writeConfigOption(final JsonWriter writer, final ConfigOption option) throws IOException {
		writer.name(option.getName());
		writer.value(option.getValue());
	}

	private void writeConfigOptionList(final JsonWriter writer, final ConfigOptionList option) throws IOException {
		writer.name(option.getName());
		writer.beginArray();
		for(final String value : option.getValues()) {
			writer.value(value);
		}
		writer.endArray();
	}

	private Gson getGson() {
		final GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		return builder.create();
	}

}
